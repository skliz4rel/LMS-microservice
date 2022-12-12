package com.lms.api.util;

import com.lms.api.constants.NotificationStatusCode;
import com.lms.api.model.request.LoanRequest;
import com.lms.api.model.response.LoanResponse;
import com.lms.api.model.response.StatusResponse;
import com.lms.api.models.response.APIResponse;
import com.lms.api.models.response.KycscoreResponse;
import com.lms.api.services.dbservices.LoanServiceImpl;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
@AllArgsConstructor
@Component
public class LoanHelper {

    private final LoanServiceImpl loanService;

    public Mono<ResponseEntity> processLoan_returnResp(String finalTransactionId, HttpHeaders responseHeaders, LoanRequest dto, APIResponse apiResponse){

        if(apiResponse.getStatusCode().equals(NotificationStatusCode.OK.getCode())){

            KycscoreResponse kycscoreResponse = (KycscoreResponse) apiResponse.getData();

            double limitAmount = !StringUtil.isNullOrEmpty(kycscoreResponse.getLimitAmount()) ?
                    Double.parseDouble(kycscoreResponse.getLimitAmount().trim())
                    : 0;


            if(limitAmount >= dto.getAmount()){ //Eligible for loan

                Mono<String> monoloanId = loanService.saveLoan_returnID(finalTransactionId, dto.getCustomerNumber(), dto.getAmount(), kycscoreResponse.getScore());

                String loanId = monoloanId.block();

                LoanResponse loanResponse = new LoanResponse();
                loanResponse.setLoanId(loanId);
                loanResponse.setAmount(dto.getAmount());
                loanResponse.setActive(true);
                loanResponse.setCustomerNumber(dto.getCustomerNumber());
                loanResponse.setPaybackDate(LocalDate.now().plusDays(10).toString());
                loanResponse.setComment("Successful loan create. You loan is now active");

                return Mono.just(ResponseEntity.status(HttpStatus.OK)
                        .headers(responseHeaders).body(loanResponse));
            }
            else{

                LoanResponse loanResponse = new LoanResponse();
                loanResponse.setComment("You request for an amount that is more than your limit");

                return Mono.just( ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .headers(responseHeaders).body(loanResponse));
            }
        }
        else{

            LoanResponse loanResponse = new LoanResponse();
            loanResponse.setComment("We cant give no a Loan we have difficulties retrieving your KYC score");

            return Mono.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .headers(responseHeaders).body(loanResponse));
        }
    }




    Mono<ResponseEntity> returnWhenEmpty(String loanId, HttpHeaders responseHeaders ){

        StatusResponse statusResponse = new StatusResponse(NotificationStatusCode.SERVICE_NOT_FOUND.getCode(), loanId, false, "No record of Loan was found");

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(responseHeaders).body(statusResponse));
    }


}