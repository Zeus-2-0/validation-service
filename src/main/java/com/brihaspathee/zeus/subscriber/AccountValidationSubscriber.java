package com.brihaspathee.zeus.subscriber;

import com.brihaspathee.zeus.broker.producer.AccountValidationResultProducer;
import com.brihaspathee.zeus.validator.AccountValidationResult;
import com.brihaspathee.zeus.validator.ValidationResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.BaseSubscriber;

import java.util.List;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 12, October 2022
 * Time: 6:04 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.subscriber
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccountValidationSubscriber<T> extends BaseSubscriber<T> {

    /**
     * Producer to send the validation result to member management service
     */
    private final AccountValidationResultProducer accountValidationResultProducer;

    /**
     * The method that is called when the validation of the account is completed
     * @param value
     */
    @Override
    protected void hookOnNext(T value) {
        log.info("Inside hookOnNext:{}");
        ValidationResult<AccountValidationResult> validationResult =
                (ValidationResult<AccountValidationResult>) value;
        log.info("Validation Result:{}", validationResult.getValidationResult().getAccountNumber());
        try {
            accountValidationResultProducer.sendAccountValidationResult(validationResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void hookOnComplete() {
        log.info("The result is produced and sent to member management service");
    }

    @Override
    protected void hookOnError(Throwable throwable) {
        log.info(throwable.getMessage());
    }
}
