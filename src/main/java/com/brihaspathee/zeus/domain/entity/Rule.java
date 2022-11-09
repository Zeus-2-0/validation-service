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
 * Time: 4:36 PM
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
@Table(name = "RULE")
public class Rule {

    /**
     * Primary key of the table
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "uuid-char")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "rule_sk", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID ruleSK;

    /**
     * The rule set that the rule belongs
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rule_set_sk")
    private RuleSet ruleSet;

    /**
     * A unique id of the rule
     */
    @Column(name = "rule_id", length = 50, columnDefinition = "varchar", nullable = false)
    private String ruleId;

    /**
     * Name of the rule
     */
    @Column(name = "rule_name", length = 100, columnDefinition = "varchar", nullable = false)
    private String ruleName;

    /**
     * A short description of the rule
     */
    @Column(name = "rule_desc", length = 200, columnDefinition = "varchar", nullable = false)
    private String ruleDesc;

    /**
     * Identifies if the rule is active
     */
    @Column(name = "active")
    private boolean active;

    /**
     * Indicates if the rule applies at the member level
     */
    @Column(name = "member_level")
    private boolean memberLevel;

    /**
     * The implementation class of the rule
     */
    @Column(name = "rule_impl_name", length = 100, columnDefinition = "varchar", nullable = false)
    private String ruleImplName;

    /**
     * The transaction type associated with the rules
     */
    @OneToMany(mappedBy = "rule", fetch = FetchType.EAGER)
    private Set<RuleTransaction> ruleTransactions;

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
        return "Rule{" +
                "ruleSK=" + ruleSK +
                ", ruleSet=" + ruleSet +
                ", ruleId='" + ruleId + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", ruleDesc='" + ruleDesc + '\'' +
                ", active=" + active +
                ", memberLevel=" + memberLevel +
                ", ruleImplName='" + ruleImplName + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
