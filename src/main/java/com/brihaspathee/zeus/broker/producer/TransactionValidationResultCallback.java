package com.brihaspathee.zeus.broker.producer;

import com.brihaspathee.zeus.message.ZeusMessagePayload;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 13, October 2022
 * Time: 7:24 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.broker.producer
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class TransactionValidationResultCallback implements
        ListenableFutureCallback<SendResult<String, ZeusMessagePayload<TransactionValidationResult>>> {

    /**
     * The validation result that was sent back
     */
    private TransactionValidationResult validationResult;

    /**
     * This method is invoked when a failure to publish a message occurs
     * @param ex
     */
    @Override
    public void onFailure(Throwable ex) {
        log.info("The message failed to publish");
    }

    /**
     * This method is invoked when the message was successfully sent
     * @param result
     */
    @Override
    public void onSuccess(SendResult<String, ZeusMessagePayload<TransactionValidationResult>> result) {
        log.info("The message successfully published");
    }
}
