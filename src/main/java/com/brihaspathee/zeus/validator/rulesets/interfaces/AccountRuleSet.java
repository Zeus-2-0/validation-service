package com.brihaspathee.zeus.validator.rulesets.interfaces;

import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.dto.rules.RuleSetDto;
import com.brihaspathee.zeus.validator.AccountValidationResult;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 02, October 2022
 * Time: 5:13 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface AccountRuleSet {

    /**
     * Validate the details of an account
     * @param accountValidationResult
     * @param accountDto
     * @param ruleSet
     */
    void validate(AccountValidationResult accountValidationResult,
                  AccountDto accountDto,
                  RuleSetDto ruleSet);
}
