package com.brihaspathee.zeus.validator;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import lombok.*;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 12, October 2022
 * Time: 6:17 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator
 * To change this template use File | Settings | File and Code Template
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResponse<T> {

    /**
     * The payload tracker that is associated with the respective validation payload
     */
    private PayloadTracker payloadTracker;

    /**
     * The respective validation result
     */
    private T validationResult;
}
