package com.brihaspathee.zeus.validator.rules.interfaces;

import com.brihaspathee.zeus.domain.entity.Rule;
import com.brihaspathee.zeus.validator.AccountValidationResult;
import com.brihaspathee.zeus.web.model.AccountDto;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 04, October 2022
 * Time: 6:17 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface AccountRule {

    /**
     * Execute the rule
     * @param accountValidationResult
     * @param accountDto
     * @param rule
     */
    void execute(AccountValidationResult accountValidationResult,
                 AccountDto accountDto,
                 Rule rule);
}
