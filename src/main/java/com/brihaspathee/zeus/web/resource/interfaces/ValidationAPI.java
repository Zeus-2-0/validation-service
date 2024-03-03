package com.brihaspathee.zeus.web.resource.interfaces;

import com.brihaspathee.zeus.web.response.ZeusApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 01, March 2024
 * Time: 3:51 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.web.resource.interfaces
 * To change this template use File | Settings | File and Code Template
 */
@RequestMapping("/api/v1/zeus/validation")
@Validated
public interface ValidationAPI {

    /**
     * Clean up the entire db
     * @return
     */
    @Operation(
            operationId = "Delete all data",
            method = "DELETE",
            description = "Delete all data",
            tags = {"validation"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Data deleted successfully",
                    content = {
                            @Content(mediaType = "application/json",schema = @Schema(implementation = ZeusApiResponse.class))
                    })
    })
    @DeleteMapping("/delete")
    ResponseEntity<ZeusApiResponse<String>> cleanUp();
}
