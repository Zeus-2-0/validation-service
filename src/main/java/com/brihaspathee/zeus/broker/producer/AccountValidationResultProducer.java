package com.brihaspathee.zeus.broker.producer;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.PayloadTrackerDetail;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerDetailHelper;
import com.brihaspathee.zeus.validator.AccountValidationResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
     * Object mapper that can covert the object into a string
     */
    private final ObjectMapper objectMapper;

    /**
     * Payload tracker detail helper instance to create the payload tracker detail record
     */
    private final PayloadTrackerDetailHelper payloadTrackerDetailHelper;

    /**
     * Send the account validation result back to member management service
     * @param payloadTracker
     * @param accountValidationResult
     */
    public void sendAccountValidationResult(PayloadTracker payloadTracker,
                                            AccountValidationResult accountValidationResult) throws JsonProcessingException {
        log.info("Inside the account validation producer:{}", accountValidationResult);
        // Convert the payload as String
        String valueAsString = objectMapper.writeValueAsString(accountValidationResult);

        // Store the response in the detail table
        PayloadTrackerDetail payloadTrackerDetail = PayloadTrackerDetail.builder()
                .payloadTracker(payloadTracker)
                .responseTypeCode("RESPONSE")
                .responsePayload(valueAsString)
                .build();
        payloadTrackerDetailHelper.createPayloadTrackerDetail(payloadTrackerDetail);
    }
}
