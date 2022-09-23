package com.brihaspathee.zeus.consumer;

import com.brihaspathee.zeus.message.AccountValidationMessage;
import com.brihaspathee.zeus.message.ZeusMessagePayload;
import com.brihaspathee.zeus.web.model.AccountDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

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

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "ZEUS.VALIDATOR.ACCOUNT")
    public void listen(ConsumerRecord<String, ZeusMessagePayload<AccountValidationMessage>> consumerRecord) throws JsonProcessingException {
        Headers headers = consumerRecord.headers();
        log.info("Headers are:");
        headers.forEach(header -> {
            log.info("key: {}, value: {}", header.key(), new String(header.value()));
        });
        String valueAsString = objectMapper.writeValueAsString(consumerRecord.value());
        ZeusMessagePayload<AccountValidationMessage> messagePayload = objectMapper.readValue(valueAsString, new TypeReference<ZeusMessagePayload<AccountValidationMessage>>(){});
        log.info(messagePayload.getPayload().getAccountDto().getAccountNumber());
        AccountDto accountDto = messagePayload.getPayload().getAccountDto();
        log.info("Total number of enrollment spans:{}", accountDto.getEnrollmentSpans().size());
    }
}
