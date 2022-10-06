package com.brihaspathee.zeus.broker.producer;

import com.brihaspathee.zeus.message.ZeusMessagePayload;
import com.brihaspathee.zeus.validator.AccountValidationResult;
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
 * Date: 06, October 2022
 * Time: 11:59 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.broker.producer
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class AccountValidationResultCallback implements
        ListenableFutureCallback<SendResult<String, ZeusMessagePayload<AccountValidationResult>>> {

    /**
     * The message payload that was sent to the topic
     */
    private AccountValidationResult messagePayload;

    /**
     * Executed when there is a failure to publish the message
     * @param ex
     */
    @Override
    public void onFailure(Throwable ex) {
        log.info("The message failed to publish");
    }

    /**
     * Executed when the message was published successfully
     * @param result
     */
    @Override
    public void onSuccess(SendResult<String, ZeusMessagePayload<AccountValidationResult>> result) {
        log.info("The message successfully published");
    }
}
