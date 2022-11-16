package com.brihaspathee.zeus.helper.interfaces;

import com.brihaspathee.zeus.domain.entity.RuleSetImplementation;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 12, November 2022
 * Time: 6:29 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface RuleSetImplementationHelper {

    /**
     * Find the rule set implementation by rule set id
     * @param ruleSetId
     * @return
     */
    RuleSetImplementation getRuleSetImplementation(String ruleSetId);
}
