package com.brihaspathee.zeus.broker.producer;

import com.brihaspathee.zeus.constants.ZeusServiceNames;
import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.PayloadTrackerDetail;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerDetailHelper;
import com.brihaspathee.zeus.message.MessageMetadata;
import com.brihaspathee.zeus.message.ZeusMessagePayload;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import com.brihaspathee.zeus.validator.ValidationResponse;
import com.brihaspathee.zeus.validator.result.ProcessingValidationResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

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
@Component
@RequiredArgsConstructor
public class ProcessingValidationResultProducer {

    /**
     * Kafka template to produce and send messages
     */
    private final KafkaTemplate<String, ZeusMessagePayload<ProcessingValidationResult>> kafkaTemplate;

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
    private final ProcessingValidationResultCallback processingValidationResultCallback;

    /**
     * Send the processing validation result back to the account processing service
     * @param validationResponse
     */
    public void sendProcessingValidationResult(ValidationResponse<ProcessingValidationResult> validationResponse) throws JsonProcessingException {
        log.info("Inside the processing validation producer:{}", validationResponse.getValidationResult());
        // Create the result payload that is to be sent to the member management service
        String[] messageDestinations = {ZeusServiceNames.ACCOUNT_PROCESSOR_SERVICE};
        ZeusMessagePayload<ProcessingValidationResult> messagePayload = ZeusMessagePayload.<ProcessingValidationResult>builder()
                .messageMetadata(MessageMetadata.builder()
                        .messageSource(ZeusServiceNames.VALIDATION_SERVICE)
                        .messageDestination(messageDestinations)
                        .messageCreationTimestamp(LocalDateTime.now())
                        .build())
                .payload(validationResponse.getValidationResult())
                .build();
        // Create the payload tracker detail record for the validation result payload
        PayloadTrackerDetail payloadTrackerDetail = createPayloadTrackerDetail(
                validationResponse.getPayloadTracker(),
                messagePayload);
        processingValidationResultCallback.setProcessingValidationResult(validationResponse.getValidationResult());
        log.info("Processing Validation Result to be sent back:{}", messagePayload.getPayload());
        // Build the producer record
        ProducerRecord<String, ZeusMessagePayload<ProcessingValidationResult>> producerRecord =
                buildProducerRecord("Test Payload id", messagePayload);
        // Send to kafka topic
        kafkaTemplate.send(producerRecord);//.addCallback(transactionValidationResultCallback);
        log.info("After the sending the validation response to account processing service is called");
    }

    /**
     * Create the payload tracker detail record
     * @param payloadTracker
     * @param messagePayload
     * @return
     * @throws JsonProcessingException
     */
    private PayloadTrackerDetail createPayloadTrackerDetail(
            PayloadTracker payloadTracker,
            ZeusMessagePayload<ProcessingValidationResult> messagePayload) throws JsonProcessingException {
        // Convert the payload as String
        String valueAsString = objectMapper.writeValueAsString(messagePayload);
        // Store the response in the detail table
        PayloadTrackerDetail payloadTrackerDetail = PayloadTrackerDetail.builder()
                .payloadTracker(payloadTracker)
                .responseTypeCode("RESULT")
                .responsePayload(valueAsString)
                .responsePayloadId(messagePayload.getPayload().getResponseId())
                .payloadDirectionTypeCode("OUTBOUND")
                .sourceDestinations(StringUtils.join(
                        messagePayload.getMessageMetadata().getMessageDestination(),
                        ','))
                .build();
        payloadTrackerDetailHelper.createPayloadTrackerDetail(payloadTrackerDetail);
        return payloadTrackerDetail;
    }

    /**
     * The method to build the producer record
     * @param payloadId
     * @param messagePayload
     */
    private ProducerRecord<String, ZeusMessagePayload<ProcessingValidationResult>> buildProducerRecord(
            String payloadId,
            ZeusMessagePayload<ProcessingValidationResult> messagePayload){
        RecordHeader messageHeader = new RecordHeader("Processing payload response id",
                "Processing payload response id".getBytes());
        return new ProducerRecord<>("ZEUS.VALIDATOR.PROCESSING.RESP",
                null,
                payloadId,
                messagePayload,
                Arrays.asList(messageHeader));
    }

}
