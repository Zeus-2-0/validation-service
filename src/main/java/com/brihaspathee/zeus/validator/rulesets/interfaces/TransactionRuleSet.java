package com.brihaspathee.zeus.validator.rulesets.interfaces;

import com.brihaspathee.zeus.domain.entity.RuleSetImplementation;
import com.brihaspathee.zeus.dto.rules.RuleSetDto;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.validator.TransactionValidationResult;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 07, November 2022
 * Time: 6:53 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rulesets.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface TransactionRuleSet {

    /**
     * Validate the details of the transaction
     * @param transactionValidationResult
     * @param transactionDto
     * @param ruleSet
     */
    void validate(TransactionValidationResult transactionValidationResult,
                  TransactionDto transactionDto,
                  RuleSetDto ruleSet,
                  RuleSetImplementation ruleSetImplementation);
}
