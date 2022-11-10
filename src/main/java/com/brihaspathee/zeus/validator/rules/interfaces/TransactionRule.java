package com.brihaspathee.zeus.validator.rules.interfaces;

import com.brihaspathee.zeus.dto.rules.RuleDto;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.validator.TransactionValidationResult;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 07, November 2022
 * Time: 6:58 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rules.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface TransactionRule {

    /**
     * Execute the rule for the transaction
     * @param transactionValidationResult
     * @param transactionDto
     * @param rule
     */
    void execute(TransactionValidationResult transactionValidationResult,
                 TransactionDto transactionDto,
                 RuleDto rule);
}
