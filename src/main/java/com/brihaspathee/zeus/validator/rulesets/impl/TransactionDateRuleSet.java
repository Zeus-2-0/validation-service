package com.brihaspathee.zeus.validator.rulesets.impl;

import com.brihaspathee.zeus.domain.entity.RuleSetImplementation;
import com.brihaspathee.zeus.dto.rules.RuleSetDto;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import com.brihaspathee.zeus.validator.rulesets.interfaces.TransactionRuleSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 17, February 2024
 * Time: 6:05 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rulesets.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("transactionDateRuleSet")
public class TransactionDateRuleSet implements TransactionRuleSet {
    @Override
    public void validate(TransactionValidationResult transactionValidationResult,
                         TransactionDto transactionDto,
                         RuleSetDto ruleSet,
                         RuleSetImplementation ruleSetImplementation) {
        log.info("test");
    }
}
