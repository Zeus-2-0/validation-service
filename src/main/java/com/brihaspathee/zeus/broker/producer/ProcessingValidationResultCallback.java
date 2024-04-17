package com.brihaspathee.zeus.broker.producer;

import com.brihaspathee.zeus.message.ZeusMessagePayload;
import com.brihaspathee.zeus.validator.result.ProcessingValidationResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 03, April 2024
 * Time: 11:15 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.broker.producer
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class ProcessingValidationResultCallback implements
        ListenableFutureCallback<SendResult<String, ZeusMessagePayload<ProcessingValidationResult>>> {

    /**
     * The message payload sent to the topic
     */
    private ProcessingValidationResult processingValidationResult;

    /**
     * Executed when there is a failure to publish the message
     * @param ex
     */
    @Override
    public void onFailure(Throwable ex) {
        log.info("The message to APS failed to publish");
    }

    /**
     * Executed when the message was published successfully
     * @param result
     */
    @Override
    public void onSuccess(SendResult<String, ZeusMessagePayload<ProcessingValidationResult>> result) {
        log.info("The message to APS published successfully");
    }
}
