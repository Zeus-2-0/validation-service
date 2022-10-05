package com.brihaspathee.zeus.validator.rulesets.impl;

import com.brihaspathee.zeus.domain.entity.Rule;
import com.brihaspathee.zeus.domain.entity.RuleSet;
import com.brihaspathee.zeus.exception.RuleImplNotFound;
import com.brihaspathee.zeus.message.AccountValidationResult;
import com.brihaspathee.zeus.validator.rules.interfaces.AccountRule;
import com.brihaspathee.zeus.validator.rulesets.interfaces.AccountRuleSet;
import com.brihaspathee.zeus.web.model.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

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
public class DemographicRuleSet implements AccountRuleSet {

    /**
     * This is a map of all the implementations of the AccountRule interface
     * The key is the camel case version of the implementation class name
     * E.g. if the class name is "DateOfBirthRule" the key is assigned as
     * "dateOfBirthRule"
     */
    private final Map<String, AccountRule> accountRules;

    /**
     * Validate the demographic details of an account
     * @param accountValidationResult
     * @param accountDto
     * @param ruleSet
     */
    @Override
    public void validate(AccountValidationResult accountValidationResult,
                                            AccountDto accountDto,
                                            RuleSet ruleSet) {
        log.info("Inside the demographic rule set");
        Set<Rule> demoRules = ruleSet.getRules();
        demoRules.stream().forEach(rule -> {
            String ruleImpl = rule.getRuleImplName();
            AccountRule accountRule = accountRules.get(ruleImpl);
            if(accountRule == null){
                throw new RuleImplNotFound("No implementation found for rule " + rule.getRuleName());
            }
            accountRule.execute(accountValidationResult,accountDto);
        });
    }
}
