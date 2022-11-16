package com.brihaspathee.zeus.validator.rulesets.impl;

import com.brihaspathee.zeus.domain.entity.RuleImplementation;
import com.brihaspathee.zeus.domain.entity.RuleSetImplementation;
import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.dto.rules.RuleDto;
import com.brihaspathee.zeus.dto.rules.RuleSetDto;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.exception.RuleImplNotFound;
import com.brihaspathee.zeus.validator.AccountValidationResult;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import com.brihaspathee.zeus.validator.rules.RuleUtil;
import com.brihaspathee.zeus.validator.rules.interfaces.AccountRule;
import com.brihaspathee.zeus.validator.rules.interfaces.TransactionRule;
import com.brihaspathee.zeus.validator.rulesets.interfaces.AccountRuleSet;
import com.brihaspathee.zeus.validator.rulesets.interfaces.TransactionRuleSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 02, October 2022
 * Time: 5:09 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("demographicRuleSet")
public class DemographicRuleSet implements AccountRuleSet, TransactionRuleSet {

    /**
     * This is a map of all the implementations of the AccountRule interface
     * The key is the camel case version of the implementation class name
     * E.g. if the class name is "DateOfBirthRule" the key is assigned as
     * "dateOfBirthRule"
     */
    private final Map<String, AccountRule> accountRules;

    /**
     * This is a map of all the implementations of the TransactionRule interface
     * The key is the camel case version of the implementation class name
     * E.g. if the class name is "DateOfBirthRule" the key is assigned as
     * "dateOfBirthRule"
     */
    private final Map<String, TransactionRule> transactionRules;

    /**
     * Validate the demographic details of an account
     * @param accountValidationResult
     * @param accountDto
     * @param ruleSet
     */
    @Override
    public void validate(AccountValidationResult accountValidationResult,
                         AccountDto accountDto,
                         RuleSetDto ruleSet,
                         RuleSetImplementation ruleSetImplementation) {
        log.info("Inside the account demographic rule set");
        // Get all the rules that are to be executed for validating the enrollment span
        List<RuleDto> demoRules = ruleSet.getRules();
        // Iterate through each rules
        demoRules.stream().forEach(rule -> {
            // Get the implementation name of the rule
            String ruleImplName = RuleUtil.getRuleImplName(ruleSetImplementation, rule.getRuleId());
            // Get the implementation of the rule that is auto wired
            AccountRule accountRule = accountRules.get(ruleImplName);
            // Generate an exception if no implementation for the rule is found
            if(accountRule == null){
                throw new RuleImplNotFound("No implementation found for rule " + rule.getRuleName());
            }
            // Execute the rule
            accountRule.execute(accountValidationResult,accountDto,rule);
        });
    }

    /**
     * Validate the demographic details of the transaction
     * @param transactionValidationResult
     * @param transactionDto
     * @param ruleSet
     */
    @Override
    public void validate(TransactionValidationResult transactionValidationResult,
                         TransactionDto transactionDto,
                         RuleSetDto ruleSet,
                         RuleSetImplementation ruleSetImplementation) {
        log.info("Inside the transaction demographic rule set");
        // Get all the rules that are to be executed for validating the demographic details of the members in the
        // transaction
        List<RuleDto> demoRules = ruleSet.getRules();
        // Iterate through each rules
        demoRules.stream().forEach(rule -> {
            // Get the implementation name of the rule
            String ruleImpl = RuleUtil.getRuleImplName(ruleSetImplementation, rule.getRuleId());;
            log.info("The rule:{}", ruleImpl);
            // Get the implementation of the rule that is auto wired
            TransactionRule transactionRule = transactionRules.get(ruleImpl);
            // Generate an exception if no implementation for the rule is found
            if(transactionRule == null){
                throw new RuleImplNotFound("No implementation found for rule " + rule.getRuleName());
            }
            // Execute the rule
            transactionRule.execute(transactionValidationResult,transactionDto,rule);
        });
    }
}
