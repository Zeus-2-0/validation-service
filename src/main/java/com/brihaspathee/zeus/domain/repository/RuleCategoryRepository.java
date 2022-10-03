package com.brihaspathee.zeus.domain.repository;

import com.brihaspathee.zeus.domain.entity.RuleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 02, October 2022
 * Time: 4:59 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.domain.repository
 * To change this template use File | Settings | File and Code Template
 */
public interface RuleCategoryRepository extends JpaRepository<RuleCategory, UUID> {

    /**
     * Find the rule category by category name
     * @param categoryName
     * @return
     */
    Optional<RuleCategory> findRuleCategoryByRuleCategoryName(String categoryName);
}
