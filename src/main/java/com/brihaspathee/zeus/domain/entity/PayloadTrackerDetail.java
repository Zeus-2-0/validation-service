package com.brihaspathee.zeus.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 25, September 2022
 * Time: 6:00 PM
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
@Table(name = "PAYLOAD_TRACKER_DETAIL")
public class PayloadTrackerDetail {

    /**
     * Primary key of the table
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "uuid-char")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "payload_tracker_detail_sk", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID payloadTrackerDetailSK;

    /**
     * The payload tracker that the details are associated
     */
    @ManyToOne
    @JoinColumn(name = "payload_tracker_sk")
    private PayloadTracker payloadTracker;

    /**
     * The type of response e.g. ACK, RESULT etc
     */
    @Column(name = "response_type_code", length = 50, columnDefinition = "varchar", nullable = false)
    private String responseTypeCode;

    /**
     * The response payload data in JSON format
     */
    @Lob
    @Column(name = "response_payload")
    private String responsePayload;

    /**
     * A unique id that is associated with the response payload
     */
    @Column(name="response_payload_id")
    private String responsePayloadId;

    /**
     * Identifies if the payload is an inbound or outbound payload
     */
    @Column(name = "payload_direction_type_code", length = 45, nullable = false)
    private String payloadDirectionTypeCode;

    /**
     * The source of the payload when its an inbound payload and the
     * destination of the payload when it is an outbound payload
     */
    @Column(name = "src_dest", length=100, nullable = false)
    private String sourceDestinations;

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
        return "PayloadTrackerDetail{" +
                "payloadTrackerDetailSK=" + payloadTrackerDetailSK +
                ", payloadTracker=" + payloadTracker +
                ", responseTypeCode='" + responseTypeCode + '\'' +
                ", responsePayload='" + responsePayload + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }

    /**
     * equals method
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayloadTrackerDetail that = (PayloadTrackerDetail) o;
        return Objects.equals(payloadTrackerDetailSK, that.payloadTrackerDetailSK);
    }

    /**
     * hashcode method
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(payloadTrackerDetailSK);
    }
}
