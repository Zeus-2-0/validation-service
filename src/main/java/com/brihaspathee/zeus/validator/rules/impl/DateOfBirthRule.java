package com.brihaspathee.zeus.validator.rules.impl;

import com.brihaspathee.zeus.domain.entity.Rule;
import com.brihaspathee.zeus.domain.entity.RuleTransaction;
import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.dto.account.MemberDto;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.dto.transaction.TransactionMemberDto;
import com.brihaspathee.zeus.validator.AccountValidationResult;
import com.brihaspathee.zeus.validator.MemberValidationResult;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import com.brihaspathee.zeus.validator.rules.RuleMessage;
import com.brihaspathee.zeus.validator.rules.RuleResult;
import com.brihaspathee.zeus.validator.rules.RuleUtil;
import com.brihaspathee.zeus.validator.rules.interfaces.AccountRule;
import com.brihaspathee.zeus.validator.rules.interfaces.TransactionRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 04, October 2022
 * Time: 6:21 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rules.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DateOfBirthRule implements AccountRule, TransactionRule {

    /**
     * Executes the date of birth rule
     * @param accountValidationResult
     * @param accountDto
     * @param rule
     */
    @Override
    public void execute(AccountValidationResult accountValidationResult,
                        AccountDto accountDto,
                        Rule rule) {
        // Date of birth rule is a member level rule, so get all the members
        Set<MemberDto> memberDtos = accountDto.getMembers();
        memberDtos.stream().forEach(memberDto -> {
            // Get member validation result object of the member by comparing the member code
            MemberValidationResult memberValidationResult = accountValidationResult.getMemberValidationResults()
                    .stream()
                    .filter(
                            temp ->
                                    temp.getMemberCode()
                                            .equals(memberDto.getMemberCode()))
                    .findFirst()
                    .orElseThrow();
            // Create the RuleResult object to store the results of the date of birth rule
            RuleResult demographicRule = RuleResult.builder()
                    .ruleId(rule.getRuleId())
                    .ruleName(rule.getRuleName())
                    .ruleMessages(new ArrayList<RuleMessage>())
                    .build();
            // get the date of birth of the member
            LocalDate dateOfBirth = memberDto.getDateOfBirth();
            // Check if date of birth is present
            if(checkIfDobIsPresent(demographicRule, dateOfBirth, true)){
                // If date of birth is present
                // Check if it is in the future
                checkIfDobIsInFuture(demographicRule, dateOfBirth, true);
                // Check if it is too far in the past (prior to 1/1/1900
                checkIfDobIsInPast(demographicRule, dateOfBirth, true);
            };
            // Check of all individual sub-rules with date of birth rule has passed
            RuleUtil.checkIfRulePassed(demographicRule);
            memberValidationResult.getRuleResults().add(demographicRule);
        });

    }

    /**
     * Validate if date of birth is present for the member
     * @param demographicRule
     * @param dateOfBirth
     * @return
     */
    private boolean checkIfDobIsPresent(RuleResult demographicRule,
                                        LocalDate dateOfBirth,
                                        boolean isAccountRule){
        if (dateOfBirth == null){
            demographicRule.getRuleMessages()
                    .add(RuleMessage.builder()
                            .messageDescription("Date of birth is not present for the member")
                            .messageCode(isAccountRule?"1500002":"1400002")
                            .messageTypeCode("CRITICAL")
                    .build());
            return false;
        }
        return true;
    }

    /**
     * Validate if date of birth is in the future
     * @param demographicRule
     * @param dateOfBirth
     */
    private void checkIfDobIsInFuture(RuleResult demographicRule,
                                      LocalDate dateOfBirth,
                                      boolean isAccountRule){
        if(dateOfBirth.isAfter(LocalDate.now())){
            demographicRule.getRuleMessages()
                    .add(RuleMessage.builder()
                            .messageDescription("Date of birth is in the future")
                            .messageCode(isAccountRule?"1500003":"1400003")
                            .messageTypeCode("CRITICAL")
                    .build());
        }
    }

    /**
     * Validate if date of birth is too far in the past
     * @param demographicRule
     * @param dateOfBirth
     */
    private void checkIfDobIsInPast(RuleResult demographicRule,
                                    LocalDate dateOfBirth,
                                    boolean isAccountRule){
        if(dateOfBirth.isBefore(LocalDate.of(1900, 01, 01))){
            demographicRule.getRuleMessages()
                    .add(RuleMessage.builder()
                            .messageDescription("Date of birth is prior to 1/1/1900")
                            .messageCode(isAccountRule?"1500004":"1400004")
                            .messageTypeCode("CRITICAL")
                    .build());
        }
    }

    /**
     * Execute the rule for the transaction
     * @param transactionValidationResult
     * @param transactionDto
     * @param rule
     */
    @Override
    public void execute(TransactionValidationResult transactionValidationResult,
                        TransactionDto transactionDto,
                        Rule rule) {
        Set<RuleTransaction> transactionTypes = rule.getRuleTransactions();
        // check if the rule has to be executed for the transaction
        if(RuleUtil.doesRuleApply(transactionTypes,
                transactionDto.getTransactionDetail().getTransactionTypeCode())){
            // Date of birth rule is a member level rule, so get all the members
            List<TransactionMemberDto> memberDtos = transactionDto.getMembers();
            memberDtos.stream().forEach(memberDto -> {
                // Get member validation result object of the member by comparing the member code
                MemberValidationResult memberValidationResult =
                        transactionValidationResult.getMemberValidationResults()
                        .stream()
                        .filter(
                                temp ->
                                        temp.getMemberCode()
                                                .equals(memberDto.getTransactionMemberCode()))
                        .findFirst()
                        .orElseThrow();
                // Create the RuleResult object to store the results of the date of birth rule
                RuleResult demographicRule = RuleResult.builder()
                        .ruleId(rule.getRuleId())
                        .ruleName(rule.getRuleName())
                        .ruleMessages(new ArrayList<RuleMessage>())
                        .build();
                // get the date of birth of the member
                LocalDate dateOfBirth = memberDto.getDateOfBirth();
                // Check if date of birth is present
                if(checkIfDobIsPresent(demographicRule, dateOfBirth, false)){
                    // If date of birth is present
                    // Check if it is in the future
                    checkIfDobIsInFuture(demographicRule, dateOfBirth, false);
                    // Check if it is too far in the past (prior to 1/1/1900
                    checkIfDobIsInPast(demographicRule, dateOfBirth, false);
                };
                // Check of all individual sub-rules with date of birth rule has passed
                RuleUtil.checkIfRulePassed(demographicRule);
                memberValidationResult.getRuleResults().add(demographicRule);
            });
        }

    }
}
