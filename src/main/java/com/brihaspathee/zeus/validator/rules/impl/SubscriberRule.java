package com.brihaspathee.zeus.validator.rules.impl;

import com.brihaspathee.zeus.dto.rules.RuleDto;
import com.brihaspathee.zeus.dto.rules.RuleTransactionDto;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.dto.transaction.TransactionMemberDto;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import com.brihaspathee.zeus.validator.rules.RuleMessage;
import com.brihaspathee.zeus.validator.rules.RuleResult;
import com.brihaspathee.zeus.validator.rules.RuleUtil;
import com.brihaspathee.zeus.validator.rules.interfaces.TransactionRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 20, February 2024
 * Time: 10:08 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rules.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("exchangeSubscriberIdRule")
public class SubscriberRule implements TransactionRule {

    /**
     * Execute the susbscriber rule
     * @param transactionValidationResult
     * @param transactionDto
     * @param rule
     */
    @Override
    public void execute(TransactionValidationResult transactionValidationResult,
                        TransactionDto transactionDto, RuleDto rule) {
        log.info("Inside subscriber presence rule");
        List<RuleTransactionDto> transactionTypes = rule.getRuleTransactions();
        // check if the rule has to be executed for the transaction
        if(RuleUtil.doesRuleApply(transactionTypes,
                transactionDto.getTransactionDetail().getTransactionTypeCode())){
            // Subscriber presence transaction level rule.
            // Create the RuleResult object to store the results of the exchange subscriber id rule
            RuleResult subscriberRule = RuleUtil.createRuleResult(rule);
            // Check if the subscriber is present in the transaction
            // Get the primary subscriber if present in the transaction
            TransactionMemberDto memberDto = RuleUtil.getPrimarySubscriber(transactionDto);
            if(memberDto == null){
                subscriberRule.getRuleMessages()
                        .add(RuleMessage.builder()
                                .messageDescription("Subscriber not present in the transaction")
                                .messageCode("1400010")
                                .messageTypeCode("CRITICAL")
                                .build());
            }
            RuleUtil.checkIfRulePassed(subscriberRule);
            transactionValidationResult.getRuleResults().add(subscriberRule);
        }
    }
}
