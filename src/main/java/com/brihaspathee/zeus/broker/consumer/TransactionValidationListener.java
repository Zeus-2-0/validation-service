package com.brihaspathee.zeus.broker.consumer;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.message.AccountValidationAcknowledgement;
import com.brihaspathee.zeus.message.AccountValidationRequest;
import com.brihaspathee.zeus.message.ZeusMessagePayload;
import com.brihaspathee.zeus.web.model.TransactionDto;
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

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 09, October 2022
 * Time: 8:18 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.broker.consumer
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionValidationListener {

    /**
     * Object mapper instance to convert the JSON to object
     */
    private final ObjectMapper objectMapper;

    /**
     * kafka consumer to consume the messages
     * @param consumerRecord
     * @return
     * @throws JsonProcessingException
     */
    @KafkaListener(topics = "ZEUS.VALIDATOR.TRANSACTION.REQ")
    // @SendTo("ZEUS.VALIDATOR.TRANSACTION.ACK")
    public ZeusMessagePayload<AccountValidationAcknowledgement> listen(
            ConsumerRecord<String, ZeusMessagePayload<TransactionDto>> consumerRecord)
            throws JsonProcessingException {
        Headers headers = consumerRecord.headers();
        log.info("Headers are:");
        headers.forEach(header -> {
            log.info("key: {}, value: {}", header.key(), new String(header.value()));
        });
        // Convert the payload as String
        String valueAsString = objectMapper.writeValueAsString(consumerRecord.value());

        // Convert it to Zeus Message Payload
        ZeusMessagePayload<TransactionDto> messagePayload = objectMapper.readValue(
                valueAsString,
                new TypeReference<ZeusMessagePayload<TransactionDto>>(){});
        log.info("Transaction received to validate:{}", messagePayload.getPayload());

        // Save the payload to the payload tracker
        // PayloadTracker payloadTracker = savePayloadTracker(valueAsString, messagePayload);

        // Create the acknowledgment back to member management service

        // ZeusMessagePayload<AccountValidationAcknowledgement> ack = createAcknowledgment(messagePayload, payloadTracker);

        // Perform validations
        // performAccountValidations(messagePayload, payloadTracker);

        log.info("After the call to validate the account");

        return null;
    }
}
