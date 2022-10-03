package com.brihaspathee.zeus.validator.interfaces;

import com.brihaspathee.zeus.message.AccountValidationResult;
import com.brihaspathee.zeus.web.model.AccountDto;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 02, October 2022
 * Time: 5:13 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface AccountDetailValidator {

    /**
     * Validate the details of an account
     * @param accountValidationResult
     * @param accountDto
     * @return
     */
    AccountValidationResult validate(AccountValidationResult accountValidationResult,
                                     AccountDto accountDto);
}
