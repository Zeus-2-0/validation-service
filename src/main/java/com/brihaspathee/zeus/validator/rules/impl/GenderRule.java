package com.brihaspathee.zeus.validator.rules.impl;

import com.brihaspathee.zeus.domain.entity.Rule;
import com.brihaspathee.zeus.validator.AccountValidationResult;
import com.brihaspathee.zeus.validator.rules.interfaces.AccountRule;
import com.brihaspathee.zeus.web.model.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 04, October 2022
 * Time: 7:07 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rules.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GenderRule implements AccountRule {

    /**
     * Execute the gender rules
     * @param accountValidationResult
     * @param accountDto
     * @param rule
     */
    @Override
    public void execute(AccountValidationResult accountValidationResult,
                        AccountDto accountDto,
                        Rule rule) {

    }
}
