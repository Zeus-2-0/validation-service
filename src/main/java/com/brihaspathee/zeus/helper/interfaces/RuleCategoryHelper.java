package com.brihaspathee.zeus.helper.interfaces;

import com.brihaspathee.zeus.domain.entity.RuleCategory;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 02, October 2022
 * Time: 5:03 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface RuleCategoryHelper {

    /**
     * Get rule category by name
     * @param ruleCategoryName
     * @return
     */
    RuleCategory getRuleCategory(String ruleCategoryName);
}
