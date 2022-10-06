package com.brihaspathee.zeus.validator;

import com.brihaspathee.zeus.exception.ValidationException;
import com.brihaspathee.zeus.validator.rules.RuleResult;
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
     * indicates if the validation passed or failed, this will indicate a failure even if
     * one of the rules at the account level or at the member level failed
     */
    private boolean validationPassed;

    /**
     * The results of the rules that were executed for the account
     */
    private List<RuleResult> ruleResults;

    /**
     * The validation results for the members who are present in the account
     */
    private List<MemberValidationResult> memberValidationResults;

    /**
     * toString
     * @return
     */
    @Override
    public String toString() {
        return "AccountValidationResult{" +
                "accountNumber='" + accountNumber + '\'' +
                ", validationPassed=" + validationPassed +
                ", ruleResults=" + ruleResults +
                ", memberValidationResults=" + memberValidationResults +
                '}';
    }
}
