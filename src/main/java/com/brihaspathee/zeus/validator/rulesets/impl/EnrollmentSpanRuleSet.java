package com.brihaspathee.zeus.validator.rulesets.impl;

import com.brihaspathee.zeus.domain.entity.RuleSetImplementation;
import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.dto.rules.RuleDto;
import com.brihaspathee.zeus.dto.rules.RuleSetDto;
import com.brihaspathee.zeus.exception.RuleSetImplNotFound;
import com.brihaspathee.zeus.validator.AccountValidationResult;
import com.brihaspathee.zeus.validator.rules.RuleUtil;
import com.brihaspathee.zeus.validator.rules.interfaces.AccountRule;
import com.brihaspathee.zeus.validator.rulesets.interfaces.AccountRuleSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 01, October 2022
 * Time: 4:45 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("enrollmentSpanRuleSet")
public class EnrollmentSpanRuleSet implements AccountRuleSet {

    /**
     * This is a map of all the implementations of the AccountRule interface
     * The key is the camel case version of the implementation class name
     * E.g. if the class name is "EnrollmentSpanOverlapRule" the key is assigned as
     * "enrollmentSpanOverlapRule"
     */
    private final Map<String, AccountRule> accountRules;

    /**
     * Method to validate the enrollment spans
     * @param accountValidationResult
     * @param accountDto
     * @param ruleSet
     */
    @Override
    public void validate(AccountValidationResult accountValidationResult,
                         AccountDto accountDto,
                         RuleSetDto ruleSet,
                         RuleSetImplementation ruleSetImplementation) {
        log.info("Inside the Enrollment span rule set");
        // Get all the rules that are to be executed for validating the enrollment span
        List<RuleDto> enrollmentSpanRules = ruleSet.getRules();
        // Iterate through each rules
        enrollmentSpanRules.stream().forEach(rule -> {
            // Get the implementation name of the rule
            String ruleImpl = RuleUtil.getRuleImplName(ruleSetImplementation, rule.getRuleId());;
            // Get the implementation of the rule that is auto wired
            AccountRule accountRule = accountRules.get(ruleImpl);
            // Generate an exception if no implementation for the rule is found
            if(accountRule == null){
                throw new RuleSetImplNotFound("No implementation found for rule " + rule.getRuleName());
            }
            // Execute the rule
            accountRule.execute(accountValidationResult, accountDto, rule);
        });
        // log.info("Account Validation Results:{}", accountValidationResult);
    }


}
