package com.brihaspathee.zeus.validator.impl;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.message.AccountValidationResult;
import com.brihaspathee.zeus.validator.interfaces.AccountValidator;
import com.brihaspathee.zeus.validator.interfaces.EnrollmentSpanValidator;
import com.brihaspathee.zeus.web.model.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 26, September 2022
 * Time: 9:22 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccountValidatorImpl implements AccountValidator {

    /**
     * Enrollment Span validator to validate the enrollment spans of the account
     */
    private final EnrollmentSpanValidator enrollmentSpanValidator;

    /**
     * The method to validate the account
     * @param payloadTracker
     * @param accountDto
     * @return
     */
    @Override
    public Mono<AccountValidationResult> validateAccount(PayloadTracker payloadTracker, AccountDto accountDto) {
//        try{
//            Thread.sleep(10000);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        log.info("Inside the account validator");
        AccountValidationResult accountValidationResult = AccountValidationResult.builder()
                .accountNumber(accountDto.getAccountNumber())
                .validationExceptions(new ArrayList<>())
                .build();
        accountValidationResult =enrollmentSpanValidator.validateEnrollmentSpans(accountValidationResult, accountDto.getEnrollmentSpans());
        return Mono.just(accountValidationResult).delayElement(Duration.ofSeconds(30));
    }
}
