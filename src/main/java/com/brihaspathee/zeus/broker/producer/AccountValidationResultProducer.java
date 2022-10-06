package com.brihaspathee.zeus.broker.producer;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.PayloadTrackerDetail;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerDetailHelper;
import com.brihaspathee.zeus.message.MessageMetadata;
import com.brihaspathee.zeus.message.ZeusMessagePayload;
import com.brihaspathee.zeus.validator.AccountValidationResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

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
     * Kafka template to produce and send messages
     */
    private final KafkaTemplate<String, ZeusMessagePayload<AccountValidationResult>> kafkaTemplate;

    /**
     * Object mapper that can covert the object into a string
     */
    private final ObjectMapper objectMapper;

    /**
     * Payload tracker detail helper instance to create the payload tracker detail record
     */
    private final PayloadTrackerDetailHelper payloadTrackerDetailHelper;

    /**
     * ListenableFutureCallback class that is used after success or failure of publishing the message
     */
    private final AccountValidationResultCallback accountValidationResultCallback;

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
        publishValidationResponse(payloadTrackerDetail.getPayloadTrackerDetailSK().toString(),
                accountValidationResult);
    }

    /**
     * Method to publish the response to the member management service
     * @param payloadId
     * @param accountValidationResult
     */
    private void publishValidationResponse(String payloadId,
                                           AccountValidationResult accountValidationResult){
        String[] messageDestinations = {"MEMBER-MGMT-SERVICE"};
        ZeusMessagePayload<AccountValidationResult> messagePayload = ZeusMessagePayload.<AccountValidationResult>builder()
                .messageMetadata(MessageMetadata.builder()
                        .messageSource("VALIDATION-SERVICE")
                        .messageDestination(messageDestinations)
                        .messageCreationTimestamp(LocalDateTime.now())
                        .build())
                .payload(accountValidationResult)
                .build();
        accountValidationResultCallback.setMessagePayload(accountValidationResult);
        ProducerRecord<String, ZeusMessagePayload<AccountValidationResult>> producerRecord =
                buildProducerRecord(payloadId, messagePayload);
        kafkaTemplate.send(producerRecord).addCallback(accountValidationResultCallback);
        log.info("After the sending the validation response to member management service is called");
    }

    /**
     * The method to build the producer record
     * @param payloadId
     * @param messagePayload
     */
    private ProducerRecord<String, ZeusMessagePayload<AccountValidationResult>> buildProducerRecord(
            String payloadId,
            ZeusMessagePayload<AccountValidationResult> messagePayload){
        RecordHeader messageHeader = new RecordHeader("payload-id",
                "test payload id".getBytes());
        return new ProducerRecord<>("ZEUS.VALIDATOR.ACCOUNT.RESP",
                null,
                payloadId,
                messagePayload,
                Arrays.asList(messageHeader));
    }
}
