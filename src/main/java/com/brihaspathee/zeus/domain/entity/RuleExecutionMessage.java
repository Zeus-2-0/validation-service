package com.brihaspathee.zeus.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 06, October 2022
 * Time: 6:42 AM
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
@Table(name = "RULE_EXECUTION_MESSAGE")
public class RuleExecutionMessage {

    /**
     * Primary key of the table
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "uuid-char")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "rule_execution_message_sk", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID ruleExecutionMessageSK;

    /**
     * The rule for which the message was generated
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rule_executed_sk")
    private RuleExecuted ruleExecuted;

    /**
     * The message code that identifies the message
     */
    @Column(name = "message_code", length = 50, columnDefinition = "varchar", nullable = false)
    private String messageCode;

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
        return "RuleExecutionMessage{" +
                "ruleExecutionMessageSK=" + ruleExecutionMessageSK +
                ", ruleExecuted=" + ruleExecuted +
                ", messageCode='" + messageCode + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
