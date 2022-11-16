package com.brihaspathee.zeus.helper.impl;

import com.brihaspathee.zeus.domain.entity.RuleSetImplementation;
import com.brihaspathee.zeus.domain.repository.RuleSetImplementationRepository;
import com.brihaspathee.zeus.exception.RuleSetImplNotFound;
import com.brihaspathee.zeus.helper.interfaces.RuleSetImplementationHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 12, November 2022
 * Time: 6:29 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RuleSetImplementationHelperImpl implements RuleSetImplementationHelper {

    /**
     * Rule set implementation repository instance to get the rule set implementations
     */
    private final RuleSetImplementationRepository repository;

    /**
     * Find the rule set implementation by rule set id
     * @param ruleSetId
     * @return
     */
    @Override
    public RuleSetImplementation getRuleSetImplementation(String ruleSetId) {
        RuleSetImplementation ruleSetImplementation =
                repository.findRuleSetImplementationByRuleSetId(ruleSetId).orElseThrow(() ->
                        {
                            throw new RuleSetImplNotFound("No implementation found for rule set " + ruleSetId);
                        });
        return ruleSetImplementation;
    }
}
