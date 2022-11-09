package com.brihaspathee.zeus.validator.interfaces;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import com.brihaspathee.zeus.validator.ValidationResponse;
import reactor.core.publisher.Mono;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 07, November 2022
 * Time: 6:35 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface TransactionValidator {

    /**
     * Validate the transaction
     * @param payloadTracker
     * @param transactionDto
     * @return
     */
    Mono<ValidationResponse<TransactionValidationResult>> validateTransaction(
            PayloadTracker payloadTracker,
            TransactionDto transactionDto);
}
