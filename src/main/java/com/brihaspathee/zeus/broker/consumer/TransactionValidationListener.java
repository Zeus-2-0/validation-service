package com.brihaspathee.zeus.broker.consumer;

import com.brihaspathee.zeus.broker.producer.TransactionValidationResultProducer;
import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.PayloadTrackerDetail;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerDetailHelper;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerHelper;
import com.brihaspathee.zeus.message.AccountValidationRequest;
import com.brihaspathee.zeus.message.Acknowledgement;
import com.brihaspathee.zeus.message.MessageMetadata;
import com.brihaspathee.zeus.message.ZeusMessagePayload;
import com.brihaspathee.zeus.util.ZeusRandomStringGenerator;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import com.brihaspathee.zeus.validator.ValidationResult;
import com.brihaspathee.zeus.web.model.AccountDto;
import com.brihaspathee.zeus.web.model.TransactionDto;
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
     * Payload tracker helper instance to create the payload tracker record
     */
    private final PayloadTrackerHelper payloadTrackerHelper;

    /**
     * Payload tracker detail helper instance to create the payload tracker detail record
     */
    private final PayloadTrackerDetailHelper payloadTrackerDetailHelper;

    /**
     * The producer that publishes the transaction validation results to transaction manager
     */
    private final TransactionValidationResultProducer transactionValidationResultProducer;

    /**
     * kafka consumer to consume the messages
     * @param consumerRecord
     * @return
     * @throws JsonProcessingException
     */
    @KafkaListener(topics = "ZEUS.VALIDATOR.TRANSACTION.REQ")
    @SendTo("ZEUS.VALIDATOR.TRANSACTION.ACK")
    public ZeusMessagePayload<Acknowledgement> listen(
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
        PayloadTracker payloadTracker = savePayloadTracker(valueAsString, messagePayload);

        // Create the acknowledgment back to transaction manager service

        ZeusMessagePayload<Acknowledgement> ack = createAcknowledgment(payloadTracker);

        // Perform validations
        performTransactionValidations(messagePayload, payloadTracker);

        log.info("After the call to validate the account");

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
    private void performTransactionValidations(ZeusMessagePayload<TransactionDto> messagePayload,
                                           PayloadTracker payloadTracker) throws JsonProcessingException{

        // TODO - Remove this temporary code when the transaction validations are implemented
        ValidationResult<TransactionValidationResult> transactionValidationResults =
                ValidationResult.<TransactionValidationResult>builder()
                        .payloadTracker(payloadTracker)
                        .validationResult(TransactionValidationResult.builder()
                                .responseId(ZeusRandomStringGenerator.randomString(15))
                                .requestPayloadId(payloadTracker.getPayloadId())
                                .ztcn(messagePayload.getPayload().getZtcn())
                                .validationPassed(true)
                                .build())
                        .build();
        // This will save the response detail in the payload tracker detail and send the message
        transactionValidationResultProducer.sendAccountValidationResult(transactionValidationResults);
//        accountValidator
//                .validateAccount(payloadTracker, accountDto)
//                .subscribe(accountValidationSubscriber);

    }

    /**
     * This method will save the payload tracker
     * @param valueAsString
     * @param messagePayload
     * @return
     */
    private PayloadTracker savePayloadTracker(String valueAsString, ZeusMessagePayload<TransactionDto> messagePayload) {
        PayloadTracker payloadTracker = PayloadTracker.builder()
                .payloadId(messagePayload.getPayloadId())
                .payload_key(messagePayload.getPayload().getZtcn())
                .payload_key_type_code("TRANSACTION")
                .payloadDirectionTypeCode("INBOUND")
                .sourceDestinations(messagePayload.getMessageMetadata().getMessageSource())
                .payload(valueAsString)
                .build();
        payloadTracker = payloadTrackerHelper.createPayloadTracker(payloadTracker);
        return payloadTracker;
    }

    /**
     * Create the acknowledgement to send back to transaction manager service
     * @param payloadTracker
     * @return
     * @throws JsonProcessingException
     */
    private ZeusMessagePayload<Acknowledgement> createAcknowledgment(
            PayloadTracker payloadTracker) throws JsonProcessingException {
        String[] messageDestinations = {"TRANSACTION-MANAGER"};
        String ackId = ZeusRandomStringGenerator.randomString(15);
        ZeusMessagePayload<Acknowledgement> ack = ZeusMessagePayload.<Acknowledgement>builder()
                .messageMetadata(MessageMetadata.builder()
                        .messageDestination(messageDestinations)
                        .messageSource("VALIDATION-SERVICE")
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
}
