package com.brihaspathee.zeus.validator.rules.impl;

import com.brihaspathee.zeus.dto.rules.RuleDto;
import com.brihaspathee.zeus.dto.rules.RuleTransactionDto;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.dto.transaction.TransactionMemberDto;
import com.brihaspathee.zeus.dto.transaction.TransactionMemberIdentifierDto;
import com.brihaspathee.zeus.validator.MemberValidationResult;
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
@Qualifier("exchangeMemberIdRule")
public class ExchangeMemberIdRule implements TransactionRule {

    /**
     * Execute the exchange member id rule
     * @param transactionValidationResult
     * @param transactionDto
     * @param rule
     */
    @Override
    public void execute(TransactionValidationResult transactionValidationResult,
                        TransactionDto transactionDto, RuleDto rule) {
        log.info("Inside exchange member id presence rule");
        List<RuleTransactionDto> transactionTypes = rule.getRuleTransactions();
        // check if the rule has to be executed for the transaction
        if(RuleUtil.doesRuleApply(transactionTypes,
                transactionDto.getTransactionDetail().getTransactionTypeCode())){
            // Exchange Member Id is a member level rule so get all the members
            List<TransactionMemberDto> memberDtos = transactionDto.getMembers();
            memberDtos.forEach(memberDto -> {
                // Get member validation result object of the member by comparing the member code
                MemberValidationResult memberValidationResult = RuleUtil.getMemberValidationResult(memberDto,
                        transactionValidationResult);
                // Create the RuleResult object to store the results of the exchange member id rule
                RuleResult exchangeMemberIdRule = RuleUtil.createRuleResult(rule);
                TransactionMemberIdentifierDto exchangeMemberId = RuleUtil.getMemberId(memberDto,
                        "EXCHMEMID");
                if(exchangeMemberId == null){
                    exchangeMemberIdRule.getRuleMessages()
                            .add(RuleMessage.builder()
                                    .messageDescription("Exchange Member Id not present for the member")
                                    .messageCode("1400001")
                                    .messageTypeCode("CRITICAL")
                                    .build());
                }
                RuleUtil.checkIfRulePassed(exchangeMemberIdRule);
                memberValidationResult.getRuleResults().add(exchangeMemberIdRule);
            });
        }
    }
}
