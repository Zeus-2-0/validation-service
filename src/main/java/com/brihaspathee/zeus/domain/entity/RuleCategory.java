package com.brihaspathee.zeus.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 02, October 2022
 * Time: 4:16 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.domain.entity
 * To change this template use File | Settings | File and Code Template
 */
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RULE_CATEGORY")
public class RuleCategory {

    /**
     * Primary key of the table
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "uuid-char")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "rule_category_sk", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID ruleCategorySK;

    /**
     * Unique id assigned to the rule category
     */
    @Column(name = "rule_category_id", length = 50, columnDefinition = "varchar", nullable = false)
    private String ruleCategoryId;

    /**
     * The name of the rule category
     */
    @Column(name = "rule_category_name", length = 100, columnDefinition = "varchar", nullable = false)
    private String ruleCategoryName;

    /**
     * A short description of the rule category
     */
    @Column(name = "rule_category_desc", length = 200, columnDefinition = "varchar", nullable = false)
    private String ruleCategoryDesc;

    /**
     * The rule sets associated with the rule category
     */
    @OneToMany(mappedBy = "ruleCategory", fetch = FetchType.EAGER)
    private Set<RuleSet> ruleSets;

    /**
     * Date when the record was created
     */
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /**
     * Date when the record was updated
     */
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
