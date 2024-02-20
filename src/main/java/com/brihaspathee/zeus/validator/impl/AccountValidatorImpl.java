package com.brihaspathee.zeus.validator.impl;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.RuleSetImplementation;
import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.dto.rules.RuleCategoryDto;
import com.brihaspathee.zeus.dto.rules.RuleSetDto;
import com.brihaspathee.zeus.dto.rules.RuleTypeDto;
import com.brihaspathee.zeus.exception.RuleSetImplNotFound;
import com.brihaspathee.zeus.helper.interfaces.RuleExecutionHelper;
import com.brihaspathee.zeus.helper.interfaces.RuleSetImplementationHelper;
import com.brihaspathee.zeus.service.interfaces.RuleService;
import com.brihaspathee.zeus.util.ZeusRandomStringGenerator;
import com.brihaspathee.zeus.validator.AccountValidationResult;
import com.brihaspathee.zeus.validator.MemberValidationResult;
import com.brihaspathee.zeus.validator.ValidationResponse;
import com.brihaspathee.zeus.validator.rules.RuleResult;
import com.brihaspathee.zeus.validator.rulesets.interfaces.AccountRuleSet;
import com.brihaspathee.zeus.validator.interfaces.AccountValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 26, September 2022
 * Time: 9:22 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccountValidatorImpl implements AccountValidator {

    /**
     * This is a map of all the implementations of the AccountRuleSet interface
     * The key is the camel case version of the implementation class name
     * E.g. if the class name is "DemographicRuleSet" the key is assigned as
     * "demographicRuleSet"
     */
    private final Map<String, AccountRuleSet> accountRuleSets;

    /**
     * Get the rules that are to be executed
     */
    private final RuleService ruleService;

    /**
     * The rule set implementation helper instance to get the implementations for each ruleset
     */
    private final RuleSetImplementationHelper ruleSetImplementationHelper;

    /**
     * The rule execution helper to save all the rules there were executed for the payload
     */
    private final RuleExecutionHelper ruleExecutionHelper;


    /**
     * The method to validate the account
     * @param payloadTracker
     * @param accountDto
     * @return
     */
    @Override
    public Mono<ValidationResponse<AccountValidationResult>> validateAccount(PayloadTracker payloadTracker, AccountDto accountDto) {
        log.info("Inside the account validator, the validators are:{}", this.accountRuleSets);
        // Get the list of all the rules for the account
        RuleCategoryDto ruleCategory = ruleService.getRules("ACCOUNT",
                "ACCOUNT_RULE");
        ValidationResponse<AccountValidationResult> validationResponse = null;
        if(ruleCategory.getRuleTypes() != null && !ruleCategory.getRuleTypes().isEmpty()){
            RuleTypeDto accountRuleType = ruleCategory.getRuleTypes().get(0);
            List<RuleSetDto> ruleSets = accountRuleType.getRuleSets();
            // Create the account validation result object with the necessary members
            // so that the results of the rules for the account and for each member can be stored
            AccountValidationResult finalAccountValidationResult =
                    constructAccountValidationResult(payloadTracker, accountDto);
            // Iterate through each rule set
            ruleSets.stream().forEach(ruleSet -> {
                log.info("Rule Set:{}", ruleSet);
                // Get the implementation of the rule set
                RuleSetImplementation ruleSetImplementation =
                        ruleSetImplementationHelper.getRuleSetImplementation(ruleSet.getRuleSetId());
                String ruleSetImplName = ruleSetImplementation.getRuleSetImplName();
                // Get the implementation class of the rule set that was auto wired
                AccountRuleSet accountRuleSet = accountRuleSets.get(ruleSetImplementation);
                // Generate an exception if no implementation is found for the rule
                if(accountRuleSet == null){
                    throw new RuleSetImplNotFound("No implementation found for rule set " + ruleSet.getRuleSetName());
                }
                // Execute all the rules withing the rule set
                accountRuleSet.validate(finalAccountValidationResult, accountDto, ruleSet, ruleSetImplementation);
            });
            // Once all the rules within the ruleset are executed check if any account or member level rules failed to
            // indicate if the validation of the account overall passed or failed
            checkIfValidationPassed(finalAccountValidationResult);
            log.info("Final Account Validation Result:{}", finalAccountValidationResult);
            saveExecutedRules(payloadTracker,finalAccountValidationResult);
            // Send the results back
            validationResponse =
                    ValidationResponse.<AccountValidationResult>builder()
                            .payloadTracker(payloadTracker)
                            .validationResult(finalAccountValidationResult)
                            .build();
        }
        return Mono.justOrEmpty(validationResponse).delayElement(Duration.ofSeconds(5));
    }

    /**
     * Method that constructs the account validation result object
     * @param accountDto
     * @return
     */
    private AccountValidationResult constructAccountValidationResult(PayloadTracker payloadTracker,
                                                                     AccountDto accountDto){
        return AccountValidationResult.builder()
                .responseId(ZeusRandomStringGenerator.randomString(15))
                .requestPayloadId(payloadTracker.getPayloadId())
                // Set the account number of the account that is to be validated
                .accountNumber(accountDto.getAccountNumber())
                // Create an empty array of rule result objects where the result of the rules will be stored
                .ruleResults(new ArrayList<RuleResult>())
                // Construct a member validation object for each member in the account
                .memberValidationResults(constructMemberValidationResult(accountDto))
                .build();
    }

    /**
     * Construct the member validation result for each member in the account
     * @param accountDto
     * @return
     */
    private List<MemberValidationResult> constructMemberValidationResult(AccountDto accountDto){
        List<MemberValidationResult> memberValidationResults =
                new ArrayList<MemberValidationResult>();
        accountDto.getMembers().stream().forEach(memberDto -> {
            memberValidationResults.add(MemberValidationResult.builder()
                    // set the member code of the member
                            .memberCode(memberDto.getMemberCode())
                    // Create an empty array of rule result objects where the result of the rules will be stored
                            .ruleResults(new ArrayList<RuleResult>())
                    .build());
        });
        return memberValidationResults;
    }

    /**
     * Check if the account validations passed or failed
     * @param accountValidationResult
     */
    private void checkIfValidationPassed(AccountValidationResult accountValidationResult){
        // Get the count of the number of account level rules that have failed
        long numberOfAccountRuleFailed = accountValidationResult.getRuleResults()
                .stream()
                .filter(ruleResult -> !ruleResult.isRulePassed()).count();
        if (numberOfAccountRuleFailed > 0){
            // if any account level rule failed then set the validation passed as false
            // It does not matter if one or more member level validations failed as well
            accountValidationResult.setValidationPassed(false);
        }else{
            // if no account level rules failed check if any member level rules failed
            accountValidationResult.getMemberValidationResults().stream().forEach(
                    memberValidationResult -> {
                long numberOfMemberRulesFailed = memberValidationResult.getRuleResults()
                        .stream()
                        .filter(ruleResult -> !ruleResult.isRulePassed()).count();
                if (numberOfMemberRulesFailed > 0){
                    // if one or more member level rules failed set the validation passed as false
                    accountValidationResult.setValidationPassed(false);
                }else {
                    // at this point all the account and member level rules have passed
                    accountValidationResult.setValidationPassed(true);
                }
            });
        }
    }

    /**
     * Save all the rules that were executed
     * @param payloadTracker
     * @param accountValidationResult
     */
    private void saveExecutedRules(PayloadTracker payloadTracker, AccountValidationResult accountValidationResult){
        if (accountValidationResult.getRuleResults() != null &&
                accountValidationResult.getRuleResults().size() > 0){
            accountValidationResult.getRuleResults().stream().forEach(ruleResult -> {
                ruleExecutionHelper.saveRulesExecuted(payloadTracker, ruleResult);
            });
        }
        if(accountValidationResult.getMemberValidationResults() != null &&
                accountValidationResult.getMemberValidationResults().size() > 0){
            accountValidationResult.getMemberValidationResults().stream().forEach(memberValidationResult -> {
                if(memberValidationResult.getRuleResults() != null &&
                        memberValidationResult.getRuleResults().size() > 0){
                    memberValidationResult.getRuleResults().stream().forEach(ruleResult -> {
                        ruleExecutionHelper.saveRulesExecuted(payloadTracker, ruleResult);
                    });
                }
            });
        }
    }
}
