package com.brihaspathee.zeus.validator.rules;

import com.brihaspathee.zeus.dto.rules.RuleTransactionDto;

import java.util.List;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 05, October 2022
 * Time: 4:06 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rules
 * To change this template use File | Settings | File and Code Template
 */
public class RuleUtil {

    /**
     * This method check the individual rule messages to check
     * if the rule overall passed or failed
     * @param ruleResult
     */
    public static void checkIfRulePassed(RuleResult ruleResult){
        long numberOfCriticalAndErrors = ruleResult.getRuleMessages().stream()
                .filter(ruleMessage ->
                        ruleMessage.getMessageTypeCode().equals("CRITICAL") ||
                                ruleMessage.getMessageCode().equals("ERROR")).count();
        if(numberOfCriticalAndErrors == 0){
            ruleResult.setRulePassed(true);
        }else{
            ruleResult.setRulePassed(false);
        }
    }

    /**
     * Check of the rule applies for the specific transaction
     * @param ruleTransactions
     * @param transactionType
     * @return
     */
    public static boolean doesRuleApply(List<RuleTransactionDto> ruleTransactions, String transactionType){
        boolean doesRuleApply =
                ruleTransactions.stream().anyMatch(
                ruleTransaction -> ruleTransaction.getTransactionTypeCode().equals(
                        transactionType));
        return doesRuleApply;
    }
}
