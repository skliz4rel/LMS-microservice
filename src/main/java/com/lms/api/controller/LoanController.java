package com.lms.api.controller;

import antlr.StringUtils;
import com.lms.api.constants.NotificationStatusCode;
import com.lms.api.constants.SystemName;
import com.lms.api.db.User;
import com.lms.api.helpers.TransactionIdGenerator;
import com.lms.api.model.request.LoanRequest;
import com.lms.api.model.response.LoanResponse;
import com.lms.api.model.response.StatusResponse;
import com.lms.api.model.response.SubscribeResponse;
import com.lms.api.models.response.APIResponse;
import com.lms.api.models.response.KycscoreResponse;
import com.lms.api.repositorys.LoanRepository;
import com.lms.api.service.ScoringService;
import com.lms.api.util.Constants;
import com.lms.api.util.LoanHelper;
import com.lms.api.utils.LoggingHelper;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


import javax.validation.Valid;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constants.LOAN_REQUEST_URL)
public class LoanController {

    private LoggingHelper loggingHelper = LoggingHelper.getInstance();
    private final TransactionIdGenerator transactionIdGenerator;

    private final LoanRepository loanRepository;

    private final ScoringService scoringService;

    private final LoanHelper loanHelper;


    @ApiOperation(value = "This operation is used for check loan status for a particular loan Id",
            notes = "This interface is used to subscribe a user for Loan service",  response = LoanResponse.class)
    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> request(
            @RequestBody @Valid LoanRequest dto,
            @RequestHeader(defaultValue = "", value = "transactionId", required = false) String transactionId
    )
    {

        if (StringUtil.isNullOrEmpty(transactionId) ) {
            transactionId = transactionIdGenerator.generateTransactionId(SystemName.MIDDLEWARE.getName());
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(Constants.TRANSACTION_ID, transactionId);

        log.info("{}:::transactionId, making a request for loan status of loan Id {}",transactionId, dto);

        String finalTransactionId = transactionId;
        return this.loanRepository.existsByCustomerNumberAndIsActiveTrue(dto.getCustomerNumber())
                .flatMap(check->{

                    if(!check){
                      return  scoringService.retrieveKYC(finalTransactionId, dto.getCustomerNumber())
                                .flatMap(apiResponse -> {

                                      return loanHelper.processLoan_returnResp( finalTransactionId,  responseHeaders,  dto,  apiResponse);

                                    });
                    }
                    else{

                        LoanResponse loanResponse = new LoanResponse();
                        loanResponse.setComment("You already have an active Loan. So you cant open another Loan during this period");


                        return Mono.just( ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .headers(responseHeaders).body(loanResponse));
                    }
                });

    }




}
