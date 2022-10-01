package com.brihaspathee.zeus.exception;

import lombok.*;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 21, September 2022
 * Time: 3:52 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.exception
 * To change this template use File | Settings | File and Code Template
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationException {

    /**
     * Validation exception code
     */
    private String exceptionCode;

    /**
     * Validation exception message
     */
    private String exceptionMessage;

    /**
     * toString method
     * @return
     */
    @Override
    public String toString() {
        return "ValidationException{" +
                "exceptionCode='" + exceptionCode + '\'' +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
