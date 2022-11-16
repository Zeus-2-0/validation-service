package com.brihaspathee.zeus.domain.repository;

import com.brihaspathee.zeus.domain.entity.RuleSetImplementation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 12, November 2022
 * Time: 6:27 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.domain.repository
 * To change this template use File | Settings | File and Code Template
 */
@Repository
public interface RuleSetImplementationRepository extends JpaRepository<RuleSetImplementation, UUID> {

    /**
     * Find rule set implementation by rule set id
     * @param ruleSetId
     * @return
     */
    Optional<RuleSetImplementation> findRuleSetImplementationByRuleSetId(String ruleSetId);
}
