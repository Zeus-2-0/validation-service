package com.brihaspathee.zeus.validator.rules.impl;

import com.brihaspathee.zeus.constants.MessageType;
import com.brihaspathee.zeus.constants.RateType;
import com.brihaspathee.zeus.constants.TransactionTypes;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 21, February 2024
 * Time: 4:25 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rules.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("premiumAmountTotalPresenceRule")
public class PremiumAmountTotalPresenceRule implements TransactionRule {

    /**
     * Validate the presence of premium amount total in the transaction
     * @param transactionValidationResult
     * @param transactionDto
     * @param rule
     */
    @Override
    public void execute(TransactionValidationResult transactionValidationResult,
                        TransactionDto transactionDto,
                        RuleDto rule) {
        log.info("Inside premium amount total presence rule");
        List<RuleTransactionDto> transactionTypes = rule.getRuleTransactions();
        // check if the rule has to be executed for the transaction
        if(RuleUtil.doesRuleApply(transactionTypes,
                transactionDto.getTransactionDetail().getTransactionTypeCode())){
            // Premium amount total is a transaction level rule.
            // Create the RuleResult object to store the results of the exchange subscriber id rule
            RuleResult premTotPresenceRule = RuleUtil.createRuleResult(rule);
            boolean isPremTotPresent = true;
            List<TransactionRateDto> transactionRates = transactionDto.getTransactionRates();
            if( transactionRates != null && !transactionRates.isEmpty()){
                Optional<TransactionRateDto> premAmtTot = transactionDto.getTransactionRates()
                        .stream()
                        .filter(transactionRateDto ->
                                transactionRateDto.getRateTypeCode().equals(RateType.PREAMTTOT.toString()))
                        .findFirst();
                if(premAmtTot.isEmpty()){
                    isPremTotPresent = false;
                }
            }else{
                isPremTotPresent = false;
            }
            if(!isPremTotPresent){
                if(transactionDto.getTransactionDetail().getTransactionTypeCode().equals(TransactionTypes.ADD.toString())){
                    premTotPresenceRule.getRuleMessages()
                            .add(RuleMessage.builder()
                                    .messageDescription("Premium amount total not present in the transaction")
                                    .messageCode("1400011")
                                    .messageTypeCode(MessageType.CRITICAL.toString())
                                    .build());
                }else{
                    premTotPresenceRule.getRuleMessages()
                            .add(RuleMessage.builder()
                                    .messageDescription("Premium amount total not present in the transaction")
                                    .messageCode("1400012")
                                    .messageTypeCode(MessageType.INFO.toString())
                                    .build());
                }

            }

            RuleUtil.checkIfRulePassed(premTotPresenceRule);
            transactionValidationResult.getRuleResults().add(premTotPresenceRule);
        }
    }
}
