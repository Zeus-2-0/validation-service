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
 * Time: 7:07 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rules.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GenderRule implements AccountRule, TransactionRule {

    /**
     * Execute the gender rules
     * @param accountValidationResult
     * @param accountDto
     * @param rule
     */
    @Override
    public void execute(AccountValidationResult accountValidationResult,
                        AccountDto accountDto,
                        Rule rule) {
        // Gender rule is a member level rule, so get all the members
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
            // Create the RuleResult object to store the results of the gender rule
            RuleResult genderRule = RuleResult.builder()
                    .ruleId(rule.getRuleId())
                    .ruleName(rule.getRuleName())
                    .ruleMessages(new ArrayList<RuleMessage>())
                    .build();
            // get the gender of the member
            String gender = memberDto.getGenderTypeCode();
            // check if gender is present
            if(checkIfGenderIsPresent(genderRule, gender, true)){
                // If date of birth is present check if its valid
                checkIfGenderIsValid(genderRule, gender, true);
            }
            // Check of all individual sub-rules with date of birth rule has passed
            RuleUtil.checkIfRulePassed(genderRule);
            memberValidationResult.getRuleResults().add(genderRule);
        });
    }

    /**
     * Validate if gender is present for the member
     * @param genderRule
     * @param gender
     * @param isAccountRule
     * @return
     */
    private boolean checkIfGenderIsPresent(RuleResult genderRule,
                                           String gender,
                                           boolean isAccountRule){
        if (gender == null){
            genderRule.getRuleMessages()
                    .add(RuleMessage.builder()
                            .messageDescription("Gender not present for the member")
                            .messageCode(isAccountRule?"1500005":"1400005")
                            .messageTypeCode("CRITICAL")
                            .build());
            return false;
        }
        return true;
    }

    /**
     * Check if the gender value is valid
     * @param genderRule
     * @param gender
     * @param isAccountRule
     */
    private void checkIfGenderIsValid(RuleResult genderRule,
                                         String gender,
                                         boolean isAccountRule){
        log.info("Inside gender rule, the gender is:{}", gender );
        if (!gender.equals("MALE") && !gender.equals("FEMALE")){
            genderRule.getRuleMessages()
                    .add(RuleMessage.builder()
                            .messageDescription("Invalid gender for the member")
                            .messageCode(isAccountRule?"1500006":"1400006")
                            .messageTypeCode("CRITICAL")
                            .build());
        }
        log.info("Gender Rule completed, the rule result is:{}", genderRule );
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
        log.info("Inside gender rule");
        Set<RuleTransaction> transactionTypes = rule.getRuleTransactions();
        // check if the rule has to be executed for the transaction
        if(RuleUtil.doesRuleApply(transactionTypes,
                transactionDto.getTransactionDetail().getTransactionTypeCode())){
            // Gender rule is a member level rule, so get all the members
            List<TransactionMemberDto> memberDtos = transactionDto.getMembers();
            memberDtos.stream().forEach(memberDto -> {
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
                // Create the RuleResult object to store the results of the gender rule
                RuleResult genderRule = RuleResult.builder()
                        .ruleId(rule.getRuleId())
                        .ruleName(rule.getRuleName())
                        .ruleMessages(new ArrayList<RuleMessage>())
                        .build();
                // get the gender of the member
                String gender = memberDto.getGenderTypeCode();
                log.debug("The gender of the member is:{}", gender);
                // check if gender is present
                if(checkIfGenderIsPresent(genderRule, gender, false)){
                    // If date of birth is present check if its valid
                    checkIfGenderIsValid(genderRule, gender, false);
                }
                // Check of all individual sub-rules with date of birth rule has passed
                RuleUtil.checkIfRulePassed(genderRule);
                memberValidationResult.getRuleResults().add(genderRule);
            });
        }

    }
}
