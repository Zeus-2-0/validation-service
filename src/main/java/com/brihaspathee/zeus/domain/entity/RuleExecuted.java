package com.brihaspathee.zeus.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 06, October 2022
 * Time: 6:28 AM
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
@Table(name = "RULE_EXECUTED")
public class RuleExecuted {

    /**
     * Primary key of the table
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "uuid-char")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "rule_executed_sk", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID ruleExecutedSK;

    /**
     * The payload for which the rules where executed
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payload_tracker_sk")
    private PayloadTracker payloadTracker;

    /**
     * The rule id that was executed
     */
    @Column(name = "rule_id", columnDefinition = "varchar", length = 50, nullable = false)
    private String ruleId;

    /**
     * Indicates if the rule passed or failed
     */
    @Column(name = "rule_passed", nullable = false)
    private boolean rulePassed;

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

    /**
     * toString method
     * @return
     */
    @Override
    public String toString() {
        return "RuleExecuted{" +
                "ruleExecutedSK=" + ruleExecutedSK +
                ", payloadTracker=" + payloadTracker +
                ", ruleId='" + ruleId + '\'' +
                ", rulePassed=" + rulePassed +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
