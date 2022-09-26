package com.brihaspathee.zeus.validator.interfaces;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.message.AccountValidationResult;
import com.brihaspathee.zeus.web.model.AccountDto;
import reactor.core.publisher.Mono;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 26, September 2022
 * Time: 9:16 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.validator.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface AccountValidator {

    /**
     * Validate the account and return the result
     * @param accountDto
     * @return
     */
    Mono<AccountValidationResult> validateAccount(PayloadTracker payloadTracker, AccountDto accountDto);
}
