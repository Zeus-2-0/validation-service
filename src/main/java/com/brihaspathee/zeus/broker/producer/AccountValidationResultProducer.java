package com.brihaspathee.zeus.broker.producer;

import com.brihaspathee.zeus.message.AccountValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 26, September 2022
 * Time: 9:28 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.broker.producer
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccountValidationResultProducer {

    /**
     * Send the account validation result back to member management servuce
     * @param accountValidationResult
     */
    public void sendAccountValidationResult(AccountValidationResult accountValidationResult){
        log.info("Inside the account validation producer:{}", accountValidationResult);
    }
}
