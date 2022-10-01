package com.brihaspathee.zeus.validator.interfaces;

import com.brihaspathee.zeus.message.AccountValidationResult;
import com.brihaspathee.zeus.web.model.EnrollmentSpanDto;

import java.util.Set;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 01, October 2022
 * Time: 4:44 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface EnrollmentSpanValidator {

    /**
     * Validates the enrollment spans in the account
     * @param accountValidationResult
     * @param enrollmentSpanDtos
     */
    AccountValidationResult validateEnrollmentSpans(AccountValidationResult accountValidationResult,
                                 Set<EnrollmentSpanDto> enrollmentSpanDtos);
}
