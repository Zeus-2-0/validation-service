package com.brihaspathee.zeus.service.impl;

import com.brihaspathee.zeus.dto.rules.RuleCategoryDto;
import com.brihaspathee.zeus.dto.rules.RuleDto;
import com.brihaspathee.zeus.reference.data.model.XWalkResponse;
import com.brihaspathee.zeus.service.interfaces.RuleService;
import com.brihaspathee.zeus.web.response.ZeusApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 10, November 2022
 * Time: 5:10 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.service.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {

    /**
     * URL for the rule service
     */
    @Value("${url.host.rules}")
    private String ruleServiceHost;

    /**
     * Webclient instance to call the reference data service
     */
    private final WebClient webClient;

    /**
     * Get all the rules by category name and rule type
     * @param ruleCategoryName
     * @param ruleType
     * @return
     */
    @Override
    public RuleCategoryDto getRules(String ruleCategoryName, String ruleType) {
        log.info("Rule Service host:{}", ruleServiceHost);
        ZeusApiResponse<RuleCategoryDto> apiResponse = webClient
                .get()
                .uri(ruleServiceHost + ruleCategoryName + "/" + ruleType)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ZeusApiResponse<RuleCategoryDto>>() {})
                .block();
        return apiResponse.getResponse();
    }
}
