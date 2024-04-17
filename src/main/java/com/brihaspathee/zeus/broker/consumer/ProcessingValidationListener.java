package com.brihaspathee.zeus.broker.consumer;

import com.brihaspathee.zeus.broker.producer.ProcessingValidationResultProducer;
import com.brihaspathee.zeus.constants.ZeusServiceNames;
import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.PayloadTrackerDetail;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerDetailHelper;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerHelper;
import com.brihaspathee.zeus.message.Acknowledgement;
import com.brihaspathee.zeus.message.MessageMetadata;
import com.brihaspathee.zeus.message.ZeusMessagePayload;
import com.brihaspathee.zeus.util.ZeusRandomStringGenerator;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import com.brihaspathee.zeus.validator.ValidationResponse;
import com.brihaspathee.zeus.validator.request.ProcessingValidationRequest;
import com.brihaspathee.zeus.validator.result.ProcessingValidationResult;
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
 * Date: 03, April 2024
 * Time: 10:57 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.broker.consumer
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessingValidationListener {

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
     * Producer to send the validation result
     */
    private final ProcessingValidationResultProducer processingValidationResultProducer;

    /**
     * Listen to the topic to receive the request from APS for processing validation
     * @param consumerRecord
     * @return
     * @throws JsonProcessingException
     */
    @KafkaListener(topics = "ZEUS.VALIDATOR.PROCESSING.REQ")
    @SendTo("ZEUS.VALIDATOR.PROCESSING.ACK")
    public ZeusMessagePayload<Acknowledgement> listen(
            ConsumerRecord<String, ZeusMessagePayload<ProcessingValidationRequest>> consumerRecord)
            throws JsonProcessingException {
        Headers headers = consumerRecord.headers();
        log.info("Headers are:");
        headers.forEach(header -> {
            log.info("key: {}, value: {}", header.key(), new String(header.value()));
        });

        // Convert the payload as String
        String valueAsString = objectMapper.writeValueAsString(consumerRecord.value());

        // Convert it to Zeus Message Payload
        ZeusMessagePayload<ProcessingValidationRequest> messagePayload = objectMapper.readValue(
                valueAsString,
                new TypeReference<ZeusMessagePayload<ProcessingValidationRequest>>(){});
        log.info("APS Request received to validate:{}", messagePayload.getPayload());

        // Save the payload to the payload tracker
        PayloadTracker payloadTracker = savePayloadTracker(valueAsString, messagePayload);

        // Create the acknowledgment back to transaction manager service

        ZeusMessagePayload<Acknowledgement> ack = createAcknowledgment(payloadTracker);

        // Perform validations
        performProcessingValidations(messagePayload, payloadTracker);

        log.info("After the call to validate the transaction");

        return ack;
    }
    /**
     * This method performs the following functions
     * 1. Perform the validations on the transaction data
     * 2. Sends the response back to transaction manager service
     * 3. Saves the response payload detail
     * @param messagePayload
     * @param payloadTracker
     */
    private void performProcessingValidations(ZeusMessagePayload<ProcessingValidationRequest> messagePayload,
                                              PayloadTracker payloadTracker) throws JsonProcessingException {
        ValidationResponse<ProcessingValidationResult> validationResponse = ValidationResponse.<ProcessingValidationResult>builder()
                .payloadTracker(payloadTracker)
                .validationResult(ProcessingValidationResult.builder()
                        .responseId(ZeusRandomStringGenerator.randomString(15))
                        .requestPayloadId(payloadTracker.getPayloadId())
                        .validationPassed(true)
                        .validationRequest(messagePayload.getPayload())
                        .build())
                .build();
        processingValidationResultProducer.sendProcessingValidationResult(validationResponse);
    }

    /**
     * Create the acknowledgement to send back to transaction manager service
     * @param payloadTracker
     * @return
     * @throws JsonProcessingException
     */
    private ZeusMessagePayload<Acknowledgement> createAcknowledgment(
            PayloadTracker payloadTracker) throws JsonProcessingException {
        String[] messageDestinations = {ZeusServiceNames.ACCOUNT_PROCESSOR_SERVICE};
        String ackId = ZeusRandomStringGenerator.randomString(15);
        ZeusMessagePayload<Acknowledgement> ack = ZeusMessagePayload.<Acknowledgement>builder()
                .messageMetadata(MessageMetadata.builder()
                        .messageDestination(messageDestinations)
                        .messageSource(ZeusServiceNames.VALIDATION_SERVICE)
                        .messageCreationTimestamp(LocalDateTime.now())
                        .build())
                .payload(Acknowledgement.builder()
                        .ackId(ackId)
                        .requestPayloadId(payloadTracker.getPayloadId())
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
    private PayloadTracker savePayloadTracker(String valueAsString, ZeusMessagePayload<ProcessingValidationRequest> messagePayload) {
        PayloadTracker payloadTracker = PayloadTracker.builder()
                .payloadId(messagePayload.getPayloadId())
                .payload_key(messagePayload.getPayload().getZrcn())
                .payload_key_type_code(messagePayload.getPayload().getZrcnTypeCode())
                .payloadDirectionTypeCode("INBOUND")
                .sourceDestinations(messagePayload.getMessageMetadata().getMessageSource())
                .payload(valueAsString)
                .build();
        payloadTracker = payloadTrackerHelper.createPayloadTracker(payloadTracker);
        return payloadTracker;
    }
}
