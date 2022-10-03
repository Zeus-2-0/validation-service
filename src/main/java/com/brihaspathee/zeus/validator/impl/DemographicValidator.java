package com.brihaspathee.zeus.validator.impl;

import com.brihaspathee.zeus.message.AccountValidationResult;
import com.brihaspathee.zeus.validator.interfaces.AccountDetailValidator;
import com.brihaspathee.zeus.web.model.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

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
@Qualifier("demographicValidator")
public class DemographicValidator implements AccountDetailValidator {

    /**
     * Validate the demographic details of an account
     * @param accountValidationResult
     * @param accountDto
     * @return
     */
    @Override
    public AccountValidationResult validate(AccountValidationResult accountValidationResult,
                                            AccountDto accountDto) {
        return null;
    }
}
