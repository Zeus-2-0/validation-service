package com.brihaspathee.zeus.helper.interfaces;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.RuleExecuted;
import com.brihaspathee.zeus.validator.rules.RuleResult;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 06, October 2022
 * Time: 6:48 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface RuleExecutionHelper {

    /**
     * Saves all the rules that were executed for a payload tracker
     * @param ruleResult
     * @return
     */
    RuleExecuted saveRulesExecuted(PayloadTracker payloadTracker,
                                   RuleResult ruleResult);
}
