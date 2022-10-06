package com.brihaspathee.zeus.validator.rules;

import lombok.*;

import java.util.List;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 05, October 2022
 * Time: 7:00 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rules
 * To change this template use File | Settings | File and Code Template
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleResult {

    /**
     * The id of the rule
     */
    private String ruleId;

    /**
     * The name of the rule
     */
    private String ruleName;

    /**
     * A boolean that indicates if the rule has passed or failed
     */
    private boolean rulePassed;

    /**
     * The messages that are the result of the execution of the rule
     * This can be empty if the rule passed successfully
     */
    private List<RuleMessage> ruleMessages;

    /**
     * toString method
     * @return
     */
    @Override
    public String toString() {
        return "RuleResult{" +
                "ruleId='" + ruleId + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", rulePassed=" + rulePassed +
                ", ruleMessages=" + ruleMessages +
                '}';
    }
}
