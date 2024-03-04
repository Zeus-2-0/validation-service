package com.brihaspathee.zeus.domain.entity;

import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import jakarta.persistence.*;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 11, November 2022
 * Time: 10:52 AM
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
@Table(name = "RULE_SET_IMPLEMENTATION")
public class RuleSetImplementation {

    /**
     * Primary key of the table
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(Types.LONGVARCHAR)
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "rule_set_impl_sk", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID ruleSetImplSK;

    /**
     * Rule set id for which the implementation is created
     */
    @Column(name = "rule_set_id", length = 50, nullable = false, columnDefinition = "varchar")
    private String ruleSetId;

    /**
     * The name of the implementation for the rule
     */
    @Column(name = "rule_set_impl_name", length = 100, nullable = false, columnDefinition = "varchar")
    private String ruleSetImplName;

    /**
     * List of all the rule implementations for the rules within the rule set
     */
    @OneToMany(mappedBy = "ruleSetImplementation")
    private List<RuleImplementation> ruleImplementations;

    /**
     * The date when the record was created
     */
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /**
     * The date when the record was updated
     */
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    /**
     * toString method
     * @return
     */
    @Override
    public String toString() {
        return "RuleSetImplementation{" +
                "ruleSetImplSK=" + ruleSetImplSK +
                ", ruleSetId='" + ruleSetId + '\'' +
                ", ruleSetImplName='" + ruleSetImplName + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
