package com.brihaspathee.zeus.subscriber;

import com.brihaspathee.zeus.broker.producer.TransactionValidationResultProducer;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import com.brihaspathee.zeus.validator.ValidationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.BaseSubscriber;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 07, November 2022
 * Time: 4:39 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.subscriber
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionValidationSubscriber<T> extends BaseSubscriber<T> {

    /**
     * Producer to send the validation result to member management service
     */
    private final TransactionValidationResultProducer transactionValidationResultProducer;

    /**
     * Triggered once all the validations are completed
     * @param value
     */
    @Override
    protected void hookOnNext(T value) {
        log.info("Inside hookOnNext for transaction validation:{}");
        ValidationResponse<TransactionValidationResult> validationResponse =
                (ValidationResponse<TransactionValidationResult>) value;
        log.info("Validation Result:{}", validationResponse.getValidationResult().getZtcn());
        try{
            transactionValidationResultProducer.sendAccountValidationResult(validationResponse);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void hookOnComplete() {
        log.info("The result is produced and sent to transaction manager service");
    }

    @Override
    protected void hookOnError(Throwable throwable) {
        log.info(throwable.getMessage());
    }
}
