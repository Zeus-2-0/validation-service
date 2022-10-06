package com.brihaspathee.zeus.validator.impl;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.RuleCategory;
import com.brihaspathee.zeus.domain.entity.RuleSet;
import com.brihaspathee.zeus.exception.RuleSetImplNotFound;
import com.brihaspathee.zeus.helper.interfaces.RuleCategoryHelper;
import com.brihaspathee.zeus.validator.AccountValidationResult;
import com.brihaspathee.zeus.validator.MemberValidationResult;
import com.brihaspathee.zeus.validator.rules.RuleMessage;
import com.brihaspathee.zeus.validator.rules.RuleResult;
import com.brihaspathee.zeus.validator.rulesets.interfaces.AccountRuleSet;
import com.brihaspathee.zeus.validator.interfaces.AccountValidator;
import com.brihaspathee.zeus.web.model.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * The key is the camel case version of the implentation class name
     * E.g. if the class name is "DemographicRuleSet" the key is assigned as
     * "demographicRuleSet"
     */
    private final Map<String, AccountRuleSet> accountRuleSets;

    /**
     * Get the rules that are to be executed
     */
    private final RuleCategoryHelper ruleCategoryHelper;


    /**
     * The method to validate the account
     * @param payloadTracker
     * @param accountDto
     * @return
     */
    @Override
    public Mono<AccountValidationResult> validateAccount(PayloadTracker payloadTracker, AccountDto accountDto) {
        log.info("Inside the account validator, the validators are:{}", this.accountRuleSets);
        // Get the list of all the rules for the account
        RuleCategory ruleCategory = ruleCategoryHelper.getRuleCategory("ACCOUNT");
        Set<RuleSet> ruleSets = ruleCategory.getRuleSets();
        // Create the account validation result object with the necessary members
        // so that the results of the rules for the account and for each member can be stored
        AccountValidationResult finalAccountValidationResult =
                constructAccountValidationResult(accountDto);
        // Iterate through each rule set
        ruleSets.stream().forEach(ruleSet -> {
            log.info("Rule Set:{}", ruleSet);
            // Get the implementation name of the rule set
            String ruleSetImplementation = ruleSet.getRuleSetImplName();
            // Get the implementation class of the rule set that was auto wired
            AccountRuleSet accountRuleSet = accountRuleSets.get(ruleSetImplementation);
            // Generate an exception if no implementation is found for the rule
            if(accountRuleSet == null){
                throw new RuleSetImplNotFound("No implementation found for rule set " + ruleSet.getRuleSetName());
            }
            // Execute all the rules withing the rule set
            accountRuleSet.validate(finalAccountValidationResult, accountDto, ruleSet);
        });
        // Once all the rules within the ruleset are executed check if any account or member level rules failed to
        // indicate if the validation of the account overall passed or failsed
        checkIfValidationPassed(finalAccountValidationResult);
        log.info("Final Account Validation Result:{}", finalAccountValidationResult);
        // Send the results back
        return Mono.just(finalAccountValidationResult).delayElement(Duration.ofSeconds(5));
    }

    /**
     * Method that constructs the account validation result object
     * @param accountDto
     * @return
     */
    private AccountValidationResult constructAccountValidationResult(AccountDto accountDto){
        return AccountValidationResult.builder()
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
     * Check if the account validations passed or faile
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
}
