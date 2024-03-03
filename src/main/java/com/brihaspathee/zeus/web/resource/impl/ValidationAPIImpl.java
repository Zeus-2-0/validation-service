package com.brihaspathee.zeus.web.resource.impl;

import com.brihaspathee.zeus.constants.ApiResponseConstants;
import com.brihaspathee.zeus.service.interfaces.DataCleanUpService;
import com.brihaspathee.zeus.web.resource.interfaces.ValidationAPI;
import com.brihaspathee.zeus.web.response.ZeusApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 01, March 2024
 * Time: 3:54 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.web.resource.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ValidationAPIImpl implements ValidationAPI {

    /**
     * Data clean up service instance
     */
    private final DataCleanUpService dataCleanUpService;

    /**
     * Clean up the transactional data from the database
     * @return
     */
    @Override
    public ResponseEntity<ZeusApiResponse<String>> cleanUp() {
        dataCleanUpService.deleteAll();
        ZeusApiResponse<String> apiResponse = ZeusApiResponse.<String>builder()
                .response("Transaction data from validation service deleted successfully")
                .statusCode(204)
                .status(HttpStatus.NO_CONTENT)
                .developerMessage(ApiResponseConstants.SUCCESS)
                .message(ApiResponseConstants.SUCCESS_REASON)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
    }
}
