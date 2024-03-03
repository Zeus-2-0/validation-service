package com.brihaspathee.zeus.validator.impl;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.RuleSetImplementation;
import com.brihaspathee.zeus.dto.rules.RuleCategoryDto;
import com.brihaspathee.zeus.dto.rules.RuleSetDto;
import com.brihaspathee.zeus.dto.rules.RuleTypeDto;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.exception.RuleSetImplNotFound;
import com.brihaspathee.zeus.helper.interfaces.RuleExecutionHelper;
import com.brihaspathee.zeus.helper.interfaces.RuleSetImplementationHelper;
import com.brihaspathee.zeus.service.interfaces.RuleService;
import com.brihaspathee.zeus.util.ZeusRandomStringGenerator;
import com.brihaspathee.zeus.validator.MemberValidationResult;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import com.brihaspathee.zeus.validator.ValidationResponse;
import com.brihaspathee.zeus.validator.interfaces.TransactionValidator;
import com.brihaspathee.zeus.validator.rules.RuleResult;
import com.brihaspathee.zeus.validator.rulesets.interfaces.TransactionRuleSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 07, November 2022
 * Time: 6:40 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionValidatorImpl implements TransactionValidator {

    /**
     * This is a map of all the implementations of the Transaction Ruleset interface
     * The key is the camel case version of the implementation class name
     * E.g. if the class name is "DemographicRuleSet" the key is assigned as
     * "demographicRuleSet"
     */
    private final Map<String, TransactionRuleSet> transactionRuleSets;

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
     * The environment in which the service is running
     */
    private final Environment environment;
    /**
     * Validate the transaction
     * @param payloadTracker
     * @param transactionDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    @Override
    public Mono<ValidationResponse<TransactionValidationResult>> validateTransaction(PayloadTracker payloadTracker,
                                                                                     TransactionDto transactionDto) {
        log.info("Inside the transaction validator, the validators are:{}", this.transactionRuleSets);
        // Get the list of all the rules for the transaction
        RuleCategoryDto ruleCategory = ruleService.getRules("TRANSACTION",
                "TRANSACTION_DATA_VALIDATION");
        log.info("Rule Category Dto received from rule service:{}", ruleCategory);
        ValidationResponse<TransactionValidationResult> validationResponse = null;
        if(ruleCategory.getRuleTypes() != null && !ruleCategory.getRuleTypes().isEmpty()){
            RuleTypeDto businessRuleType = ruleCategory.getRuleTypes().get(0);
            List<RuleSetDto> ruleSets = businessRuleType.getRuleSets();
            // Create the transaction validation result object with the necessary members
            // so that the results of the rules for the transaction and for each member can be stored
            TransactionValidationResult transactionValidationResult =
                    constructTransactionValidationResult(payloadTracker, transactionDto);
            // Iterate through each rule set
            ruleSets.forEach(ruleSet -> {
                log.info("Rule Set:{}", ruleSet);
                // Get the implementation of the rule set
                RuleSetImplementation ruleSetImplementation =
                        ruleSetImplementationHelper.getRuleSetImplementation(ruleSet.getRuleSetId());
                String ruleSetImplName = ruleSetImplementation.getRuleSetImplName();
                // Get the implementation class of the rule set that was auto wired
                TransactionRuleSet transactionRuleSet = transactionRuleSets.get(ruleSetImplName);
                // Generate an exception if no implementation is found for the rule
                if(transactionRuleSet == null){
                    throw new RuleSetImplNotFound("No implementation found for rule set " + ruleSet.getRuleSetName());
                }
                // Execute all the rules withing the rule set
                transactionRuleSet.validate(transactionValidationResult, transactionDto, ruleSet, ruleSetImplementation);
            });
            // Once all the rules within the ruleset are executed check if any transaction or member level rules failed to
            // indicate if the validation of the account overall passed or failed
            checkIfValidationPassed(transactionValidationResult);
            log.info("Final Transaction Validation Result:{}", transactionValidationResult);
            saveExecutedRules(payloadTracker, transactionValidationResult);
            if(Arrays.asList(environment.getActiveProfiles()).contains("int-test")){
                transactionValidationResult.setTestTransactionDto(transactionDto);
            }
            // Send the results back
            validationResponse =
                    ValidationResponse.<TransactionValidationResult>builder()
                            .payloadTracker(payloadTracker)
                            .validationResult(transactionValidationResult)
                            .build();

        }
        return Mono.justOrEmpty(validationResponse);
        //return Mono.justOrEmpty(validationResponse).delayElement(Duration.ofSeconds(5));

    }

    /**
     * Construct the transaction validation result object
     * @param payloadTracker
     * @param transactionDto
     * @return
     */
    private TransactionValidationResult constructTransactionValidationResult(PayloadTracker payloadTracker,
                                                                             TransactionDto transactionDto){
        return TransactionValidationResult.builder()
                // Create a new response id for the validation result response
                .responseId(ZeusRandomStringGenerator.randomString(15))
                // The request payload id for which the response is generated
                .requestPayloadId(payloadTracker.getPayloadId())
                // The transaction control number for which the validation is performed
                .ztcn(transactionDto.getZtcn())
                // Create an empty array of rule result objects where the result of the rules will be stored
                .ruleResults(new ArrayList<RuleResult>())
                // Construct a member validation object for each member in the transaction
                .memberValidationResults(constructMemberValidationResult(transactionDto))
                .build();
    }

    /**
     * Construct the member validation result object
     * @param transactionDto
     * @return
     */
    private List<MemberValidationResult> constructMemberValidationResult(TransactionDto transactionDto){
        List<MemberValidationResult> memberValidationResults =
                new ArrayList<MemberValidationResult>();
        transactionDto.getMembers().stream().forEach(memberDto -> {
            memberValidationResults.add(MemberValidationResult.builder()
                    // set the member code of the member
                    .memberCode(memberDto.getTransactionMemberCode())
                    // Create an empty array of rule result objects where the result of the rules will be stored
                    .ruleResults(new ArrayList<RuleResult>())
                    .build());
        });
        return memberValidationResults;
    }

    /**
     * Check if the transaction validations passed or failed
     * @param transactionValidationResult
     */
    private void checkIfValidationPassed(TransactionValidationResult
                                                 transactionValidationResult){
        // Get the count of the number of transaction level rules that have failed
        long numberOfTransRuleFailed = transactionValidationResult.getRuleResults()
                .stream()
                .filter(ruleResult -> !ruleResult.isRulePassed()).count();
        log.info("Number of transaction level rules failed:{}", numberOfTransRuleFailed);
        if(numberOfTransRuleFailed > 0){
            // if any transaction level rule failed then set the validation passed as false
            // It does not matter if one or more member level validations failed as well
            transactionValidationResult.setValidationPassed(false);
        }else{
            // if no transaction level rules failed check if any member level rules failed
            log.info("Check if one of more member level rules failed");
            AtomicLong numberOfMemberRulesFailed = new AtomicLong();
            transactionValidationResult.getMemberValidationResults().stream().forEach(
                    memberValidationResult -> {
                        log.info("Member validation result to check if any rule failed:{}", memberValidationResult);
                        long rulesFailed = memberValidationResult.getRuleResults()
                                .stream()
                                .filter(ruleResult -> !ruleResult.isRulePassed()).count();
                        numberOfMemberRulesFailed.getAndAdd(rulesFailed);
                        log.info("Number of member level rules failed:{}", numberOfMemberRulesFailed);
                    });
            if(numberOfMemberRulesFailed.get() > 0){
                // if one or more member level rules failed set the validation passed as false
                transactionValidationResult.setValidationPassed(false);
            }else{
                // at this point all the transaction and member level rules have passed
                transactionValidationResult.setValidationPassed(true);
            }
        }
    }

    /**
     * Save all the rules that were executed
     * @param payloadTracker
     * @param transactionValidationResult
     */
    private void saveExecutedRules(PayloadTracker payloadTracker,
                                   TransactionValidationResult transactionValidationResult){
        if (transactionValidationResult.getRuleResults() != null &&
                transactionValidationResult.getRuleResults().size() > 0){
            transactionValidationResult.getRuleResults().stream().forEach(ruleResult -> {
                ruleExecutionHelper.saveRulesExecuted(payloadTracker, ruleResult);
            });
        }
        if(transactionValidationResult.getMemberValidationResults() != null &&
                transactionValidationResult.getMemberValidationResults().size() > 0){
            transactionValidationResult.getMemberValidationResults().stream().forEach(memberValidationResult -> {
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
