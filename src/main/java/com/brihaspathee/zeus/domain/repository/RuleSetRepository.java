package com.brihaspathee.zeus.domain.repository;

import com.brihaspathee.zeus.domain.entity.RuleSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 02, October 2022
 * Time: 4:49 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.domain.repository
 * To change this template use File | Settings | File and Code Template
 */
public interface RuleSetRepository extends JpaRepository<RuleSet, UUID> {
}
