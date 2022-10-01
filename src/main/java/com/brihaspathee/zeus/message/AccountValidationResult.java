package com.brihaspathee.zeus.message;

import com.brihaspathee.zeus.exception.ValidationException;
import lombok.*;

import java.util.List;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 26, September 2022
 * Time: 9:17 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.message
 * To change this template use File | Settings | File and Code Template
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountValidationResult {

    /**
     * The account for which the validation is being formed
     */
    private String accountNumber;

    /**
     * indicates if the validation passed or failed
     */
    private boolean validationPassed;

    /**
     * Contains the list of validation exceptions
     */
    private List<ValidationException> validationExceptions;

    /**
     * toString
     * @return
     */
    @Override
    public String toString() {
        return "AccountValidationResult{" +
                "accountNumber='" + accountNumber + '\'' +
                ", validationPassed=" + validationPassed +
                ", validationExceptions=" + validationExceptions +
                '}';
    }
}
