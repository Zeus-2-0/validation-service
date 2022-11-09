package com.brihaspathee.zeus.validator.rulesets.impl;

import com.brihaspathee.zeus.domain.entity.Rule;
import com.brihaspathee.zeus.domain.entity.RuleSet;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.exception.RuleImplNotFound;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import com.brihaspathee.zeus.validator.rules.interfaces.TransactionRule;
import com.brihaspathee.zeus.validator.rulesets.interfaces.TransactionRuleSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 08, November 2022
 * Time: 1:52 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rulesets.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RateRuleSet implements TransactionRuleSet {

    /**
     * This is a map of all the implementations of the TransactionRule interface
     * The key is the camel case version of the implementation class name
     * E.g. if the class name is "DateOfBirthRule" the key is assigned as
     * "dateOfBirthRule"
     */
    private final Map<String, TransactionRule> transactionRules;

    /**
     * Validate the rates received in the transaction
     * @param transactionValidationResult
     * @param transactionDto
     * @param ruleSet
     */
    @Override
    public void validate(TransactionValidationResult transactionValidationResult,
                         TransactionDto transactionDto,
                         RuleSet ruleSet) {
        log.info("Inside the transaction rate rule set");
        // Get all the rules that are to be executed for validating the demographic details of the members in the
        // transaction
        Set<Rule> rateRules = ruleSet.getRules();
        // Iterate through each rules
        rateRules.stream().forEach(rule -> {
            // Get the implementation name of the rule
            String ruleImpl = rule.getRuleImplName();
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
