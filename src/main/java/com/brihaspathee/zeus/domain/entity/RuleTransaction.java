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
 * Date: 09, November 2022
 * Time: 4:43 AM
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
@Table(name = "RULE_TRANSACTION")
public class RuleTransaction {

    /**
     * Primary key of the table
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "uuid-char")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "rule_transaction_sk", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID ruleTransactionSK;

    /**
     * The rule that the transaction type belongs
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rule_sk")
    private Rule rule;

    /**
     * The transaction type for which the rule is to be executed
     */
    private String transactionTypeCode;

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
        return "RuleTransaction{" +
                "ruleTransactionSK=" + ruleTransactionSK +
                ", rule=" + rule +
                ", transactionTypeCode='" + transactionTypeCode + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
