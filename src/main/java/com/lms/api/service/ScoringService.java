package com.lms.api.service;

import com.lms.api.constants.NotificationStatusCode;
import com.lms.api.db.Token;
import com.lms.api.models.error.APIError;
import com.lms.api.models.response.APIResponse;
import com.lms.api.repositorys.TokenRepository;
import com.lms.api.services.scoringapicalls.ScoringengineService;
import com.lms.api.util.MyResponseHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ScoringService {

    private final ScoringengineService scoringengineService;

    private final TokenRepository tokenRepository;

    @Retryable(value = WebClientException.class, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    public Mono<APIResponse> retrieveKYC(String transactionId, String customerNumber) throws WebClientException{

        List<Token> tokenList = tokenRepository.findAll().collectList().block();

         if(tokenList != null && tokenList.size() >0){
             // Do nothing
         }
         else{

             return Mono.just(MyResponseHelper
                     .returnNoTokenresp("You can not call the Scoring Engine because you are yet to register your middleware to get an Access Token"));
         }

           return scoringengineService.initiateQueryScore_returnKycScore(transactionId,
                   tokenList.get(0).getScoringEngineToken(),
                        customerNumber
                ).map(apiResponse -> {

                    return apiResponse;
                }).switchIfEmpty(Mono.fromCallable(() -> {

                   return MyResponseHelper.returnNoTokenresp("Invalid token, Contact the Scoring Team Administrator!");
               }));
    }


}
