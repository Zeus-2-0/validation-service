package com.brihaspathee.zeus.broker.consumer;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.PayloadTrackerDetail;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerDetailHelper;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerHelper;
import com.brihaspathee.zeus.message.AccountValidationAcknowledgement;
import com.brihaspathee.zeus.message.AccountValidationRequest;
import com.brihaspathee.zeus.message.MessageMetadata;
import com.brihaspathee.zeus.message.ZeusMessagePayload;
import com.brihaspathee.zeus.util.ZeusRandomStringGenerator;
import com.brihaspathee.zeus.web.model.AccountDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * kafka consumer to consume the messages
     * @param consumerRecord
     * @return
     * @throws JsonProcessingException
     */
    @KafkaListener(topics = "ZEUS.VALIDATOR.ACCOUNT.REQ")
    @SendTo("ZEUS.VALIDATOR.ACCOUNT.ACK")
    public ZeusMessagePayload<AccountValidationAcknowledgement> listen(
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

        // Save the payload to the payload tracjer
        PayloadTracker payloadTracker = PayloadTracker.builder()
                .payloadId(messagePayload.getPayload().getValidationMessageId())
                .payload_key(messagePayload.getPayload().getAccountDto().getAccountNumber())
                .payload_key_type_code("ACCOUNT")
                .payloadDirectionTypeCode("INBOUND")
                .sourceDestinations(messagePayload.getMessageMetadata().getMessageSource())
                .payload(valueAsString)
                .build();
        payloadTracker = payloadTrackerHelper.createPayloadTracker(payloadTracker);

        // Perform validations
        log.info(messagePayload.getPayload().getAccountDto().getAccountNumber());
        AccountDto accountDto = messagePayload.getPayload().getAccountDto();
        log.info("Total number of enrollment spans:{}", accountDto.getEnrollmentSpans().size());
        String[] messageDestinations = {"MEMBER-MGMT-SERVICE"};

        // Create the acknowledgment back to member management service
        ZeusMessagePayload<AccountValidationAcknowledgement> ack = ZeusMessagePayload.<AccountValidationAcknowledgement>builder()
                .messageMetadata(MessageMetadata.builder()
                        .messageDestination(messageDestinations)
                        .messageSource("VALIDATION-SERVICE")
                        .messageCreationTimestamp(LocalDateTime.now())
                        .build())
                .payload(AccountValidationAcknowledgement.builder()
                        .ackId(ZeusRandomStringGenerator.randomString(15))
                        .requestPayloadId(messagePayload.getPayload().getValidationMessageId())
                        .build())
                .build();
        String ackAsString = objectMapper.writeValueAsString(ack);

        // Store the acknowledgement in the detail table
        PayloadTrackerDetail payloadTrackerDetail = PayloadTrackerDetail.builder()
                .payloadTracker(payloadTracker)
                .responseTypeCode("ACKNOWLEDGEMENT")
                .responsePayload(ackAsString)
                .build();
        payloadTrackerDetailHelper.createPayloadTrackerDetail(payloadTrackerDetail);
        return ack;
    }
}
