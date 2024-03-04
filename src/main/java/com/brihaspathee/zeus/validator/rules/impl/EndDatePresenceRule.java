package com.brihaspathee.zeus.validator.rules.impl;

import com.brihaspathee.zeus.constants.TransactionTypes;
import com.brihaspathee.zeus.dto.rules.RuleDto;
import com.brihaspathee.zeus.dto.rules.RuleTransactionDto;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.dto.transaction.TransactionMemberDto;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 20, February 2024
 * Time: 10:06 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rules.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("endDatePresenceRule")
public class EndDatePresenceRule implements TransactionRule {

    /**
     * Execute the end date presence rule
     * @param transactionValidationResult
     * @param transactionDto
     * @param rule
     */
    @Override
    public void execute(TransactionValidationResult transactionValidationResult, TransactionDto transactionDto, RuleDto rule) {
        log.info("Inside end date presence rule");
        List<RuleTransactionDto> transactionTypes = rule.getRuleTransactions();
        // check if the rule has to be executed for the transaction
        if(RuleUtil.doesRuleApply(transactionTypes,
                transactionDto.getTransactionDetail().getTransactionTypeCode())){
            // End date presence is a member level rule so get all the members
            List<TransactionMemberDto> memberDtos = transactionDto.getMembers();
            memberDtos.forEach(memberDto -> {
                // Check of the member is being termed or canceled
                // End date is required only for members who are being termed or canceled
                if(memberDto.getTransactionTypeCode().equals(TransactionTypes.CANCELORTERM.toString())){
                    // Get member validation result object of the member by comparing the member code
                    MemberValidationResult memberValidationResult =
                            transactionValidationResult
                                    .getMemberValidationResults()
                                    .stream()
                                    .filter(
                                            temp ->
                                                    temp.getMemberCode()
                                                            .equals(memberDto.getTransactionMemberCode()))
                                    .findFirst()
                                    .orElseThrow();
                    // Create the RuleResult object to store the results of the end date presence
                    RuleResult endDatePresenceRule = RuleResult.builder()
                            .ruleId(rule.getRuleId())
                            .ruleName(rule.getRuleName())
                            .ruleMessages(new ArrayList<RuleMessage>())
                            .build();
                    if(memberDto.getEndDate() == null){
                        endDatePresenceRule.getRuleMessages()
                                .add(RuleMessage.builder()
                                        .messageDescription("End Date not present for the member")
                                        .messageCode("1400008")
                                        .messageTypeCode("CRITICAL")
                                        .build());
                    }
                    RuleUtil.checkIfRulePassed(endDatePresenceRule);
                    memberValidationResult.getRuleResults().add(endDatePresenceRule);
                }
            });
        }
    }
}
