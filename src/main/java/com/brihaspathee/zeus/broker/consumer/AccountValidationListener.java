package com.brihaspathee.zeus.broker.consumer;

import com.brihaspathee.zeus.broker.producer.AccountValidationResultProducer;
import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.PayloadTrackerDetail;
import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerDetailHelper;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerHelper;
import com.brihaspathee.zeus.message.*;
import com.brihaspathee.zeus.subscriber.AccountValidationSubscriber;
import com.brihaspathee.zeus.util.ZeusRandomStringGenerator;
import com.brihaspathee.zeus.validator.AccountValidationResult;
import com.brihaspathee.zeus.validator.ValidationResponse;
import com.brihaspathee.zeus.validator.interfaces.AccountValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 22, September 2022
 * Time: 3:52 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.consumer
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountValidationListener {

    /**
     * Object mapper instance to convert the JSON to object
     */
    private final ObjectMapper objectMapper;

    /**
     * Payload tracker helper instance to create the payload tracker record
     */
    private final PayloadTrackerHelper payloadTrackerHelper;

    /**
     * Payload tracker detail helper instance to create the payload tracker detail record
     */
    private final PayloadTrackerDetailHelper payloadTrackerDetailHelper;

    /**
     * Validator to validate the account detail received
     */
    private final AccountValidator accountValidator;

    /**
     * Producer to send the validation result to member management service
     */
    private final AccountValidationResultProducer accountValidationResultProducer;

    /**
     * The subscriber for the account validation
     */
    private final AccountValidationSubscriber<ValidationResponse<AccountValidationResult>> accountValidationSubscriber;

    /**
     * kafka consumer to consume the messages
     * @param consumerRecord
     * @return
     * @throws JsonProcessingException
     */
    @KafkaListener(topics = "ZEUS.VALIDATOR.ACCOUNT.REQ")
    @SendTo("ZEUS.VALIDATOR.ACCOUNT.ACK")
    public ZeusMessagePayload<Acknowledgement> listen(
            ConsumerRecord<String, ZeusMessagePayload<AccountValidationRequest>> consumerRecord)
            throws JsonProcessingException {
        Headers headers = consumerRecord.headers();
        log.info("Headers are:");
        headers.forEach(header -> {
            log.info("key: {}, value: {}", header.key(), new String(header.value()));
        });
        // Convert the payload as String
        String valueAsString = objectMapper.writeValueAsString(consumerRecord.value());

        // Convert it to Zeus Message Payload
        ZeusMessagePayload<AccountValidationRequest> messagePayload = objectMapper.readValue(
                valueAsString,
                new TypeReference<ZeusMessagePayload<AccountValidationRequest>>(){});

        // Save the payload to the payload tracker
        PayloadTracker payloadTracker = savePayloadTracker(valueAsString, messagePayload);

        // Create the acknowledgment back to member management service

        ZeusMessagePayload<Acknowledgement> ack = createAcknowledgment(messagePayload, payloadTracker);

        // Perform validations
        performAccountValidations(messagePayload, payloadTracker);
        log.info("After the call to validate the account");

        return ack;
    }

    /**
     * This method performs the following functions
     * 1. Perform the validations on the account data
     * 2. Sends the response back to member management service
     * 3. Saves the response payload detail
     * @param messagePayload
     * @param payloadTracker
     */
    private void performAccountValidations(ZeusMessagePayload<AccountValidationRequest> messagePayload, PayloadTracker payloadTracker) {
        log.info(messagePayload.getPayload().getAccountDto().getAccountNumber());
        AccountDto accountDto = messagePayload.getPayload().getAccountDto();
        log.info("Total number of enrollment spans:{}", accountDto.getEnrollmentSpans().size());

        PayloadTracker finalPayloadTracker = payloadTracker;
        accountValidator
                .validateAccount(payloadTracker, accountDto)
                .subscribe(accountValidationSubscriber);
//        accountValidator
//                .validateAccount(payloadTracker, accountDto)
//                .subscribe(
//                        accountValidationResult ->
//                        {
//                            try {
//                                accountValidationResultProducer.sendAccountValidationResult(
//                                        accountValidationResult);
//                            } catch (JsonProcessingException e) {
//                                e.printStackTrace();
//                            }
//                        },
//                        throwable -> log.info(throwable.getMessage()),
//                        () -> log.info("The result is produced and sent to member management service"));
    }

    /**
     * Create the acknowledgement to send back to member management service
     * @param messagePayload
     * @param payloadTracker
     * @return
     * @throws JsonProcessingException
     */
    private ZeusMessagePayload<Acknowledgement> createAcknowledgment(
            ZeusMessagePayload<AccountValidationRequest> messagePayload,
            PayloadTracker payloadTracker) throws JsonProcessingException {
        String[] messageDestinations = {"MEMBER-MGMT-SERVICE"};
        String ackId = ZeusRandomStringGenerator.randomString(15);
        ZeusMessagePayload<Acknowledgement> ack = ZeusMessagePayload.<Acknowledgement>builder()
                .messageMetadata(MessageMetadata.builder()
                        .messageDestination(messageDestinations)
                        .messageSource("VALIDATION-SERVICE")
                        .messageCreationTimestamp(LocalDateTime.now())
                        .build())
                .payload(Acknowledgement.builder()
                        .ackId(ackId)
                        .requestPayloadId(messagePayload.getPayload().getValidationMessageId())
                        .build())
                .build();
        String ackAsString = objectMapper.writeValueAsString(ack);

        // Store the acknowledgement in the detail table
        PayloadTrackerDetail payloadTrackerDetail = PayloadTrackerDetail.builder()
                .payloadTracker(payloadTracker)
                .responseTypeCode("ACKNOWLEDGEMENT")
                .responsePayload(ackAsString)
                .responsePayloadId(ackId)
                .payloadDirectionTypeCode("OUTBOUND")
                .sourceDestinations(StringUtils.join(messageDestinations, ','))
                .build();
        payloadTrackerDetailHelper.createPayloadTrackerDetail(payloadTrackerDetail);
        return ack;
    }

    /**
     * This method will save the payload tracker
     * @param valueAsString
     * @param messagePayload
     * @return
     */
    private PayloadTracker savePayloadTracker(String valueAsString, ZeusMessagePayload<AccountValidationRequest> messagePayload) {
        PayloadTracker payloadTracker = PayloadTracker.builder()
                .payloadId(messagePayload.getPayload().getValidationMessageId())
                .payload_key(messagePayload.getPayload().getAccountDto().getAccountNumber())
                .payload_key_type_code("ACCOUNT")
                .payloadDirectionTypeCode("INBOUND")
                .sourceDestinations(messagePayload.getMessageMetadata().getMessageSource())
                .payload(valueAsString)
                .build();
        payloadTracker = payloadTrackerHelper.createPayloadTracker(payloadTracker);
        return payloadTracker;
    }
}
