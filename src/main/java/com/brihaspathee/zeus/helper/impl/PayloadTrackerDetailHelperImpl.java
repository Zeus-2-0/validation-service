package com.brihaspathee.zeus.helper.impl;

import com.brihaspathee.zeus.domain.entity.PayloadTrackerDetail;
import com.brihaspathee.zeus.domain.repository.PayloadTrackerDetailRepository;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerDetailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 26, September 2022
 * Time: 6:22 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PayloadTrackerDetailHelperImpl implements PayloadTrackerDetailHelper {

    /**
     * Repository to perform CRUD operations
     */
    private final PayloadTrackerDetailRepository payloadTrackerDetailRepository;

    /**
     * Create a payload tracker detail record
     * @param payloadTrackerDetail
     * @return
     */
    @Override
    public PayloadTrackerDetail createPayloadTrackerDetail(PayloadTrackerDetail payloadTrackerDetail) {
        PayloadTrackerDetail savedPayloadTrackerDetail = payloadTrackerDetailRepository.save(payloadTrackerDetail);
        return savedPayloadTrackerDetail;
    }
}
