package com.brihaspathee.zeus.validator.rulesets.impl;

import com.brihaspathee.zeus.domain.entity.RuleSetImplementation;
import com.brihaspathee.zeus.dto.rules.RuleDto;
import com.brihaspathee.zeus.dto.rules.RuleSetDto;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.exception.RuleImplNotFound;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import com.brihaspathee.zeus.validator.rules.RuleUtil;
import com.brihaspathee.zeus.validator.rules.interfaces.TransactionRule;
import com.brihaspathee.zeus.validator.rulesets.interfaces.TransactionRuleSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 17, February 2024
 * Time: 6:05 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rulesets.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("dataIntegrityRuleSet")
public class DataIntegrityRuleSet implements TransactionRuleSet {

    /**
     * This is a map of all the implementations of the TransactionRule interface
     * The key is the camel case version of the implementation class name
     * E.g. if the class name is "DateOfBirthRule" the key is assigned as
     * "dateOfBirthRule"
     */
    private final Map<String, TransactionRule> transactionRules;

    /**
     * Validate the transaction using the rules in the rule set
     * @param transactionValidationResult
     * @param transactionDto
     * @param ruleSet
     * @param ruleSetImplementation
     */
    @Override
    public void validate(TransactionValidationResult transactionValidationResult,
                         TransactionDto transactionDto,
                         RuleSetDto ruleSet,
                         RuleSetImplementation ruleSetImplementation) {
        log.info("Inside the data integrity rule set");
        // Get all the rules that are to be executed for validating transaction data
        // Some of these rules can be at the transaction level and some are at the member level
        List<RuleDto> dataIntegrityRules = ruleSet.getRules();
        // Iterate through each rules
        dataIntegrityRules.forEach(rule -> {
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
