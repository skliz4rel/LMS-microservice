package com.lms.api.controller;


import com.lms.api.constants.NotificationStatusCode;
import com.lms.api.model.request.SubscribeRequest;
import com.lms.api.helpers.TransactionIdGenerator;
import com.lms.api.constants.SystemName;
import com.lms.api.models.error.APIError;
import com.lms.api.models.response.APIResponse;
import com.lms.api.repositorys.TokenRepository;
import com.lms.api.services.dbservices.UserServiceImpl;
import com.lms.api.services.soapservices.apicall.CustomerService;
import com.lms.api.services.soapservices.customer.CustomerResponse;
import com.lms.api.util.Constants;
import com.lms.api.util.MyResponseHelper;
import com.lms.api.util.SubscribeHelper;
import com.lms.api.utils.LoggingHelper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constants.SUBSCRIPTION_URL)
public class SubscribeController {

    private final CustomerService customerService;
    private LoggingHelper loggingHelper = LoggingHelper.getInstance();
    private final TransactionIdGenerator transactionIdGenerator;

    private final TokenRepository tokenRepository;

    private final UserServiceImpl userService;

    private final SubscribeHelper subscribeHelper;

    @ApiOperation(value = "This operation is subscribe a new user or customer with phone number",
            notes = "This interface is used to subscribe a user for Loan service",  response = APIResponse.class)
    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> onboard(@Valid @RequestBody SubscribeRequest request,
                                           @RequestHeader(defaultValue = "", value = "transactionId", required = false) String transactionId
    ) {

        loggingHelper.requestObject(log, request);

        if (transactionId == null) {
            transactionId = transactionIdGenerator.generateTransactionId(SystemName.MIDDLEWARE.getName());
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(Constants.TRANSACTION_ID, transactionId);

        log.info("{}:::transactionId, making a request to subscribe the customer via request {}",transactionId, request);

        CustomerResponse customerResponse =  customerService.getKyc(transactionId, request.getCustomerNumber());

        if(customerResponse != null){

            APIResponse notfoundResp  = MyResponseHelper.noDataResp("No KYC record was found for this customer Phone number "+request.getCustomerNumber());
            return Mono.just( ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(responseHeaders).body(notfoundResp));
        }

       return  this.userService.storeUser(transactionId, customerResponse)
                .flatMap(check->{
                    if(check) {
                        return Mono.just(ResponseEntity.status(HttpStatus.OK)
                                .headers(responseHeaders).body(subscribeHelper.returnSuccessResp(request)));
                    }
                    else {

                        APIResponse errResponse = subscribeHelper.returnFailedResp(request);

                        return Mono.just( ResponseEntity.status(errResponse.getError().getStatus())
                                .headers(responseHeaders).body(errResponse));
                    }
                });
    }


}
