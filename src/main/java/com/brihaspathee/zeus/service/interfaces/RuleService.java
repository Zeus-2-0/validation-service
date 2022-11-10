package com.brihaspathee.zeus.service.interfaces;

import com.brihaspathee.zeus.dto.rules.RuleCategoryDto;
import com.brihaspathee.zeus.dto.rules.RuleDto;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 10, November 2022
 * Time: 5:10 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.service.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface RuleService {

    /**
     * Get all the rules by category name and rule type
     * @param ruleCategoryName
     * @param ruleType
     * @return
     */
    RuleCategoryDto getRules(String ruleCategoryName,
                             String ruleType);
}
