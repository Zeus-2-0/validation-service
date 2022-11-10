package com.brihaspathee.zeus.validator.rules.impl;

import com.brihaspathee.zeus.dto.rules.RuleDto;
import com.brihaspathee.zeus.dto.rules.RuleTransactionDto;
import com.brihaspathee.zeus.dto.transaction.TransactionDto;
import com.brihaspathee.zeus.dto.transaction.TransactionMemberDto;
import com.brihaspathee.zeus.dto.transaction.TransactionRateDto;
import com.brihaspathee.zeus.validator.TransactionValidationResult;
import com.brihaspathee.zeus.validator.rules.RuleMessage;
import com.brihaspathee.zeus.validator.rules.RuleResult;
import com.brihaspathee.zeus.validator.rules.RuleUtil;
import com.brihaspathee.zeus.validator.rules.interfaces.TransactionRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 08, November 2022
 * Time: 2:14 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rules.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumTotalRule implements TransactionRule {

    /**
     * Execute the rate rule
     * @param transactionValidationResult
     * @param transactionDto
     * @param rule
     */
    @Override
    public void execute(TransactionValidationResult transactionValidationResult,
                        TransactionDto transactionDto,
                        RuleDto rule) {
        log.info("About to execute the rate rule");
        List<RuleTransactionDto> transactionTypes = rule.getRuleTransactions();
        // check if the rule has to be executed for the transaction
        if(RuleUtil.doesRuleApply(transactionTypes,
                transactionDto.getTransactionDetail().getTransactionTypeCode())){
            // Premium total is a transaction level rule
            // Create the RuleResult object to store the results of the rate rule
            RuleResult rateRule = RuleResult.builder()
                    .ruleId(rule.getRuleId())
                    .ruleName(rule.getRuleName())
                    .ruleMessages(new ArrayList<RuleMessage>())
                    .build();
            checkPremiumTotalRuleOne(rateRule, transactionDto);
            checkPremiumTotalRuleTwo(rateRule, transactionDto);
            // Check of all individual sub-rules with date of birth rule has passed
            RuleUtil.checkIfRulePassed(rateRule);
            transactionValidationResult.getRuleResults().add(rateRule);
        }
    }

    /**
     * This rule checks the below equation
     * PRE AMT TOT = TOT RES AMT + APTC AMT + OTH PAY AMT 1 + OTH PAY AMT 2
     * @param rateRule
     * @param transactionDto
     */
    private void checkPremiumTotalRuleOne(RuleResult rateRule,
                                          TransactionDto transactionDto){
        Optional<TransactionRateDto> preAmt = transactionDto.getTransactionRates()
                .stream()
                .filter(
                        rateDto -> rateDto.getRateTypeCode().equals("PREAMTTOT"))
                .findFirst();
        if (preAmt.isPresent()){
            Optional<TransactionRateDto> resAmt = transactionDto.getTransactionRates()
                    .stream()
                    .filter(
                            rateDto -> rateDto.getRateTypeCode().equals("TOTRESAMT"))
                    .findFirst();
            Optional<TransactionRateDto> aptc = transactionDto.getTransactionRates()
                    .stream()
                    .filter(
                            rateDto -> rateDto.getRateTypeCode().equals("APTCAMT"))
                    .findFirst();
            Optional<TransactionRateDto> othPay1 = transactionDto.getTransactionRates()
                    .stream()
                    .filter(
                            rateDto -> rateDto.getRateTypeCode().equals("OTHERPAYAMT1"))
                    .findFirst();
            Optional<TransactionRateDto> othPay2 = transactionDto.getTransactionRates()
                    .stream()
                    .filter(
                            rateDto -> rateDto.getRateTypeCode().equals("OTHERPAYAMT2"))
                    .findFirst();
            BigDecimal preAmtTot = preAmt.get().getTransactionRate();
            BigDecimal totResAmt = resAmt.isPresent()?resAmt.get().getTransactionRate(): BigDecimal.valueOf(0);
            BigDecimal aptcAmt = aptc.isPresent()?aptc.get().getTransactionRate(): BigDecimal.valueOf(0);
            BigDecimal othPayAmt1 = othPay1.isPresent()?othPay1.get().getTransactionRate(): BigDecimal.valueOf(0);
            BigDecimal othPayAmt2 = othPay2.isPresent()?othPay2.get().getTransactionRate(): BigDecimal.valueOf(0);
            BigDecimal validationAmt = preAmtTot
                                        .subtract(totResAmt)
                                        .subtract(aptcAmt)
                                        .subtract(othPayAmt1)
                                        .subtract(othPayAmt2);
            log.info("Validation Amount:{}", validationAmt);
            if(validationAmt.compareTo(BigDecimal.valueOf(0)) != 0){
                rateRule.getRuleMessages()
                        .add(RuleMessage.builder()
                                .messageDescription("Premium Amount Total is not equal to sum of APTC, Total Responsible" +
                                        " Amount and other pay amounts")
                                .messageCode("1500007")
                                .messageTypeCode("CRITICAL")
                                .build());
            }
        }else{
            return;
        }
    }

    /**
     * This rule checks the below equation
     * PRE AMT TOT = Rates of the individual members
     * @param rateRule
     * @param transactionDto
     */
    private void checkPremiumTotalRuleTwo(RuleResult rateRule,
                                          TransactionDto transactionDto){
        Optional<TransactionRateDto> preAmt = transactionDto.getTransactionRates()
                .stream()
                .filter(
                        rateDto -> rateDto.getRateTypeCode().equals("PREAMTTOT"))
                .findFirst();
        if(preAmt.isPresent()){
            List<TransactionMemberDto> memberDtos = transactionDto.getMembers();
            List<BigDecimal> memberRates =
                    memberDtos.stream()
                            .map(
                                    memberDto -> memberDto.getMemberRate())
                            .collect(Collectors.toList());
            BigDecimal memberRateSum =
                    memberRates.stream()
                            .reduce(BigDecimal.ZERO, (p, q) -> p.add(q));
            if(memberRateSum.compareTo(preAmt.get().getTransactionRate()) != 0){
                rateRule.getRuleMessages()
                        .add(RuleMessage.builder()
                                .messageDescription("Sum of individual member rates not equal to premium amount total")
                                .messageCode("1500008")
                                .messageTypeCode("CRITICAL")
                                .build());
            }
        }

    }
}
