package com.brihaspathee.zeus.validator.rules.impl;

import com.brihaspathee.zeus.domain.entity.Rule;
import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.dto.account.EnrollmentSpanDto;
import com.brihaspathee.zeus.validator.AccountValidationResult;
import com.brihaspathee.zeus.validator.rules.RuleMessage;
import com.brihaspathee.zeus.validator.rules.RuleResult;
import com.brihaspathee.zeus.validator.rules.RuleUtil;
import com.brihaspathee.zeus.validator.rules.interfaces.AccountRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 05, October 2022
 * Time: 2:24 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.rules.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentSpanOverlapRule implements AccountRule {

    /**
     * Check if the enrollment spans in the account overlap
     * @param accountValidationResult
     * @param accountDto
     * @param rule
     */
    @Override
    public void execute(AccountValidationResult accountValidationResult,
                        AccountDto accountDto,
                        Rule rule) {
        // Build the enrollment span overlap rule result object to store the result
        RuleResult enrollmentSpanOverlapRule = RuleResult.builder()
                .ruleId(rule.getRuleId())
                .ruleName(rule.getRuleName())
                .ruleMessages(new ArrayList<RuleMessage>())
                .build();
        // Get all the enrollment spans
        Set<EnrollmentSpanDto> enrollmentSpanDtos = accountDto.getEnrollmentSpans();
        // Check if any of the enrollment spans are overlapping
        if(checkForOverlappingSpans(enrollmentSpanDtos)){
            enrollmentSpanOverlapRule.getRuleMessages().add(RuleMessage.builder()
                            .messageTypeCode("CRITICAL")
                            .messageCode("1500001")
                            .messageDescription("One or more enrollment spans are overlapping")
                    .build());
        }
        RuleUtil.checkIfRulePassed(enrollmentSpanOverlapRule);
        accountValidationResult.getRuleResults().add(enrollmentSpanOverlapRule);
    }

    /**
     * Check if the enrollment spans overlap
     * @param enrollmentSpanDtos
     * @return
     */
    private boolean checkForOverlappingSpans(Set<EnrollmentSpanDto> enrollmentSpanDtos){
        if(enrollmentSpanDtos!=null &&
                !enrollmentSpanDtos.isEmpty()){
            // Continue to check for enrollment span overlap if there is at least one or more enrollment spans
            // Sort all the non-canceled enrollment spans by the start date of the enrollment spans
            List<EnrollmentSpanDto> sortedEnrollmentSpans = enrollmentSpanDtos.stream()
                    .filter(enrollmentSpanDto -> !enrollmentSpanDto.getStatusTypeCode().equals("CANCEL"))
                    .sorted(Comparator.comparing(EnrollmentSpanDto::getStartDate))
                    .collect(Collectors.toList());
            // Get the enrollment span size
            int enrollmentSpanSize = sortedEnrollmentSpans.size();
            // Check if the enrollment span size is greater than 1
            if (enrollmentSpanSize != 1) {
                // If it is greater than 1 get first enrollment span and store it as previous enrollment span
                EnrollmentSpanDto prevEnrollmentSpan = sortedEnrollmentSpans.get(0);
                for (int i = 1; i < enrollmentSpanSize; i++) {
                    // Iterate through the rest of the enrollment span
                    EnrollmentSpanDto currentEnrollmentSpan = sortedEnrollmentSpans.get(i);
                    // check if the dates of the enrollment span overlap
                    if (isDateOverlap(prevEnrollmentSpan.getEndDate(), currentEnrollmentSpan.getStartDate())) {
                        return true;
                    }
                    // If the dates of the previous two enrollment span does not overlap then set the previous
                    // enrollment span as the current enrollment span and continue to loop through
                    prevEnrollmentSpan = currentEnrollmentSpan;
                }
            }
        }
        // if no overlapping enrollment spans found return false
        return false;
    }

    /**
     * Check if the dates of the span overlap
     * @param previousSpanEnDate
     * @param currentSpanStartDate
     * @return
     */
    private boolean isDateOverlap(final LocalDate previousSpanEnDate, final LocalDate currentSpanStartDate){
//        log.info("Previous span end date:{}", previousSpanEnDate);
//        log.info("Current span start date:{}", currentSpanStartDate);
//        log.info("previousSpanEnDate.isAfter(currentSpanStartDate):{}", previousSpanEnDate.isAfter(currentSpanStartDate));

        // Date of the enrollment spans overlap of the end date of the first enrollment span is greater than the
        // Start date of the next enrollment span
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
