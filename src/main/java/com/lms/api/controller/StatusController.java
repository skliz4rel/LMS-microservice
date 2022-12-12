package com.lms.api.controller;

import com.lms.api.constants.NotificationStatusCode;
import com.lms.api.constants.SystemName;
import com.lms.api.helpers.TransactionIdGenerator;
import com.lms.api.model.response.StatusResponse;
import com.lms.api.repositorys.LoanRepository;
import com.lms.api.util.Constants;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constants.LOAN_REQUEST_URL)
public class StatusController {

    private LoggingHelper loggingHelper = LoggingHelper.getInstance();
    private final TransactionIdGenerator transactionIdGenerator;

    private final LoanRepository loanRepository;


    @ApiOperation(value = "This operation is used for check loan status for a particular loan Id",
            notes = "This interface is used to check loan status by LoanId",  response = StatusResponse.class)

    @GetMapping(value="/{loanId}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> status(@PathVariable String loanId,
                                       @RequestHeader(defaultValue = "", value = "transactionId", required = false) String transactionId
    ){
        if (StringUtil.isNullOrEmpty(transactionId )) {
            transactionId = transactionIdGenerator.generateTransactionId(SystemName.MIDDLEWARE.getName());
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(Constants.TRANSACTION_ID, transactionId);

        log.info("{}:::transactionId, making a check for loan status of loan Id {}",transactionId, loanId);

        return this.loanRepository.existsByLoanIdAndIsActiveTrue(loanId)
                .flatMap(active->{
                    StatusResponse statusResponse; HttpStatus status;

                    if(active){
                        status = HttpStatus.OK;
                        statusResponse = new StatusResponse(NotificationStatusCode.OK.getCode(), loanId, active, "Loan is active");
                    }
                    else{
                        status= HttpStatus.NOT_FOUND;
                        statusResponse = new StatusResponse(NotificationStatusCode.SERVICE_NOT_FOUND.getCode(),loanId, active, "Loan is inactive");
                    }

                    return Mono.just(ResponseEntity.status(status)
                            .headers(responseHeaders).body(statusResponse));
                });
        //.switchIfEmpty(returnWhenEmpty(loanId, responseHeaders));

    }



}
