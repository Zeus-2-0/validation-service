package com.brihaspathee.zeus.validator.impl;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.RuleCategory;
import com.brihaspathee.zeus.helper.interfaces.RuleCategoryHelper;
import com.brihaspathee.zeus.message.AccountValidationResult;
import com.brihaspathee.zeus.validator.interfaces.AccountDetailValidator;
import com.brihaspathee.zeus.validator.interfaces.AccountValidator;
import com.brihaspathee.zeus.web.model.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;

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
     * This is a map of all the implementations of the AccountDetailValidator interface
     * The key is the camel case version of the implentation class name
     * E.g. if the class name is "DemographicValidator" the key is assigned as
     * "demographicValidator"
     */
    private final Map<String, AccountDetailValidator> accountDetailValidators;

    /**
     * Get the rules that are to be executed
     */
    private final RuleCategoryHelper ruleCategoryHelper;


    /**
     * The method to validate the account
     * @param payloadTracker
     * @param accountDto
     * @return
     */
    @Override
    public Mono<AccountValidationResult> validateAccount(PayloadTracker payloadTracker, AccountDto accountDto) {
        log.info("Inside the account validator, the validators are:{}", this.accountDetailValidators);
        // Get the list of all the rules
        RuleCategory ruleCategory = ruleCategoryHelper.getRuleCategory("ACCOUNT");
        AccountValidationResult accountValidationResult = AccountValidationResult.builder()
                .accountNumber(accountDto.getAccountNumber())
                .validationExceptions(new ArrayList<>())
                .build();
        accountValidationResult =accountDetailValidators.get("enrollmentSpanValidator").validate(accountValidationResult, accountDto);
        return Mono.just(accountValidationResult).delayElement(Duration.ofSeconds(30));
    }
}
