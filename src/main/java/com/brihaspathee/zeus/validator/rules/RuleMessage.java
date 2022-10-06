package com.brihaspathee.zeus.validator.rules;

import lombok.*;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 05, October 2022
 * Time: 7:03 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rules
 * To change this template use File | Settings | File and Code Template
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleMessage {

    /**
     * A unique code for the message
     */
    private String messageCode;

    /**
     * The message type that identifies the type of message
     * E.g. INFO, WARNING, CRITICAL
     */
    private String messageTypeCode;

    /**
     * A short description of the message
     */
    private String messageDescription;

    /**
     * toString method
     * @return
     */
    @Override
    public String toString() {
        return "RuleMessage{" +
                "messageCode='" + messageCode + '\'' +
                ", messageTypeCode='" + messageTypeCode + '\'' +
                ", messageDescription='" + messageDescription + '\'' +
                '}';
    }
}
