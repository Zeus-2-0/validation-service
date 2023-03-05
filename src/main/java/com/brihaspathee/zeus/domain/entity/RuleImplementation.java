package com.brihaspathee.zeus.domain.entity;

import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import jakarta.persistence.*;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 12, November 2022
 * Time: 6:18 AM
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
@Table(name = "RULE_IMPLEMENTATION")
public class RuleImplementation {

    /**
     * Primary key of the table
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(Types.LONGVARCHAR)
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "rule_impl_sk", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID ruleImplSK;

    /**
     * The rule set implementation that the rule belongs
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rule_set_impl_sk")
    private RuleSetImplementation ruleSetImplementation;

    /**
     * Unique id assigned to each rule
     */
    @Column(name = "rule_id", length = 50, columnDefinition = "varchar", nullable = false)
    private String ruleId;

    /**
     * The name of the rule implementation
     */
    @Column(name = "rule_impl_name", length = 50, columnDefinition = "varchar", nullable = false)
    private String ruleImplName;

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
        return "RuleImplementation{" +
                "ruleImplSK=" + ruleImplSK +
                ", ruleSetImplementation=" + ruleSetImplementation +
                ", ruleId='" + ruleId + '\'' +
                ", ruleImplName='" + ruleImplName + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
