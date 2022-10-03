package com.brihaspathee.zeus.helper.impl;

import com.brihaspathee.zeus.domain.entity.RuleCategory;
import com.brihaspathee.zeus.domain.repository.RuleCategoryRepository;
import com.brihaspathee.zeus.helper.interfaces.RuleCategoryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 02, October 2022
 * Time: 5:04 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RuleCategoryHelperImpl implements RuleCategoryHelper {

    /**
     * Rule category repository to get the rules
     */
    private final RuleCategoryRepository repository;

    /**
     * Get rule category by name
     * @param ruleCategoryName
     * @return
     */
    @Override
    public RuleCategory getRuleCategory(String ruleCategoryName) {
        return repository.findRuleCategoryByRuleCategoryName(ruleCategoryName).orElseThrow();
    }
}
