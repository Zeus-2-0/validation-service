package com.brihaspathee.zeus.helper.impl;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.RuleExecuted;
import com.brihaspathee.zeus.domain.entity.RuleExecutionMessage;
import com.brihaspathee.zeus.domain.repository.RuleExecutedRepository;
import com.brihaspathee.zeus.domain.repository.RuleExecutionMessageRepository;
import com.brihaspathee.zeus.helper.interfaces.RuleExecutionHelper;
import com.brihaspathee.zeus.validator.rules.RuleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 06, October 2022
 * Time: 6:49 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RuleExecutionHelperImpl implements RuleExecutionHelper {

    /**
     * Rule executed repository to store all the rules that were executed for the payload
     */
    private final RuleExecutedRepository ruleExecutedRepository;

    /**
     * Messages that were created as part of executing the rule
     */
    private final RuleExecutionMessageRepository ruleExecutionMessageRepository;

    /**
     * Saves all the rules that were executed for a payload tracker
     * @param payloadTracker
     * @param ruleResult
     * @return
     */
    @Override
    public RuleExecuted saveRulesExecuted(PayloadTracker payloadTracker,
                                          RuleResult ruleResult) {
        // If rule result is null there is nothing to save return null
        if(ruleResult == null ){
            return null;
        }
        // create the rule executed entity and save
        RuleExecuted ruleExecuted = RuleExecuted.builder()
                .payloadTracker(payloadTracker)
                .ruleId(ruleResult.getRuleId())
                .rulePassed(ruleResult.isRulePassed())
                .build();
        ruleExecuted = ruleExecutedRepository.save(ruleExecuted);
        // check if there are any messages present in the rule result, if there are any present save it
        if(ruleResult.getRuleMessages() != null && ruleResult.getRuleMessages().size() > 0){
            RuleExecuted finalRuleExecuted = ruleExecuted;
            ruleResult.getRuleMessages().stream().forEach(ruleMessage -> {
                RuleExecutionMessage ruleExecutionMessage = RuleExecutionMessage.builder()
                        .ruleExecuted(finalRuleExecuted)
                        .messageCode(ruleMessage.getMessageCode())
                        .build();
                ruleExecutionMessageRepository.save(ruleExecutionMessage);
            });
        }
        return ruleExecuted;
    }
}
