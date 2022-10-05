package com.brihaspathee.zeus.validator.rules.impl;

import com.brihaspathee.zeus.exception.ValidationException;
import com.brihaspathee.zeus.message.AccountValidationResult;
import com.brihaspathee.zeus.validator.rules.interfaces.AccountRule;
import com.brihaspathee.zeus.web.model.AccountDto;
import com.brihaspathee.zeus.web.model.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 04, October 2022
 * Time: 6:21 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rules.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DateOfBirthRule implements AccountRule {

    /**
     * Executes the date of birth rule
     * @param accountValidationResult
     * @param accountDto
     */
    @Override
    public void execute(AccountValidationResult accountValidationResult, AccountDto accountDto) {
        Set<MemberDto> memberDtos = accountDto.getMembers();
        memberDtos.stream().forEach(memberDto -> {
            LocalDate dateOfBirth = memberDto.getDateOfBirth();
            if(checkIfDobIsPresent(accountValidationResult, dateOfBirth)){
                checkIfDobIsInFuture(accountValidationResult, dateOfBirth);
                checkIfDobIsInPast(accountValidationResult, dateOfBirth);
            };
        });

    }

    /**
     * Validate if date of birth is present for the member
     * @param accountValidationResult
     * @param dateOfBirth
     * @return
     */
    private boolean checkIfDobIsPresent(AccountValidationResult accountValidationResult, LocalDate dateOfBirth){
        if (dateOfBirth == null){
            accountValidationResult.getValidationExceptions().add(ValidationException.builder()
                            .exceptionMessage("Date of birth is not present for the member")
                            .exceptionCode("150002")
                    .build());
            return false;
        }
        return true;
    }

    /**
     * Validate if date of birth is in the future
     * @param accountValidationResult
     * @param dateOfBirth
     */
    private void checkIfDobIsInFuture(AccountValidationResult accountValidationResult, LocalDate dateOfBirth){
        if(dateOfBirth.isAfter(LocalDate.now())){
            accountValidationResult.getValidationExceptions().add(ValidationException.builder()
                    .exceptionMessage("Date of birth is in the future")
                    .exceptionCode("150003")
                    .build());
        }
    }

    /**
     * Validate if date of birth is too far in the past
     * @param accountValidationResult
     * @param dateOfBirth
     */
    private void checkIfDobIsInPast(AccountValidationResult accountValidationResult, LocalDate dateOfBirth){
        if(dateOfBirth.isBefore(LocalDate.of(1900, 01, 01))){
            accountValidationResult.getValidationExceptions().add(ValidationException.builder()
                    .exceptionMessage("Date of birth is prior to 1/1/1900")
                    .exceptionCode("150004")
                    .build());
        }
    }
}
