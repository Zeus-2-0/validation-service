package com.brihaspathee.zeus.validator.rulesets.impl;

import com.brihaspathee.zeus.domain.entity.RuleSet;
import com.brihaspathee.zeus.exception.ValidationException;
import com.brihaspathee.zeus.message.AccountValidationResult;
import com.brihaspathee.zeus.validator.rulesets.interfaces.AccountRuleSet;
import com.brihaspathee.zeus.web.model.AccountDto;
import com.brihaspathee.zeus.web.model.EnrollmentSpanDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 01, October 2022
 * Time: 4:45 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("enrollmentSpanRuleSet")
public class EnrollmentSpanRuleSet implements AccountRuleSet {

    /**
     * Method to validate the enrollment spans
     * @param accountValidationResult
     * @param accountDto
     * @param ruleSet
     */
    @Override
    public void validate(AccountValidationResult accountValidationResult,
                                            AccountDto accountDto,
                                            RuleSet ruleSet) {
        Set<EnrollmentSpanDto> enrollmentSpanDtos = accountDto.getEnrollmentSpans();
        if(checkForOverlappingSpans(enrollmentSpanDtos)){
            accountValidationResult.getValidationExceptions()
                    .add(ValidationException.builder()
                            .exceptionCode("1500001")
                            .exceptionMessage("One or more enrollment spans are overlapping")
                            .build());
        }
        log.info("Account Validation Results:{}", accountValidationResult);
    }

    /**
     * Check if the enrollment spans overlap
     * @param enrollmentSpanDtos
     * @return
     */
    private boolean checkForOverlappingSpans(Set<EnrollmentSpanDto> enrollmentSpanDtos){
        if(enrollmentSpanDtos!=null &&
                !enrollmentSpanDtos.isEmpty()){
            List<EnrollmentSpanDto> sortedEnrollmentSpans = enrollmentSpanDtos.stream()
                    .sorted(Comparator.comparing(EnrollmentSpanDto::getStartDate))
                    .collect(Collectors.toList());
            int enrollmentSpanSize = sortedEnrollmentSpans.size();
            if (enrollmentSpanSize != 1) {
                EnrollmentSpanDto prevEnrollmentSpan = sortedEnrollmentSpans.get(0);
                for (int i = 1; i < enrollmentSpanSize; i++) {
                    EnrollmentSpanDto currentEnrollmentSpan = sortedEnrollmentSpans.get(i);
                    if (isDateOverlap(prevEnrollmentSpan.getEndDate(), currentEnrollmentSpan.getStartDate())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if the dates of the span overlap
     * @param previousSpanEnDate
     * @param currentSpanStartDate
     * @return
     */
    private boolean isDateOverlap(final LocalDate previousSpanEnDate, final LocalDate currentSpanStartDate){
        log.info("Previous span end date:{}", previousSpanEnDate);
        log.info("Current span start date:{}", currentSpanStartDate);
        log.info("previousSpanEnDate.isAfter(currentSpanStartDate):{}", previousSpanEnDate.isAfter(currentSpanStartDate));
        if(previousSpanEnDate == null || currentSpanStartDate == null){
            return false;
        }
        if (previousSpanEnDate.equals(currentSpanStartDate) || previousSpanEnDate.isAfter(currentSpanStartDate)){
            return true;
        }else{
            return false;
        }
    }
}
