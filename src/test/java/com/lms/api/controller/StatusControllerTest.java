package com.lms.api.controller;

import com.lms.api.LMSApplication;
import com.lms.api.constants.NotificationStatusCode;
import com.lms.api.db.Loan;
import com.lms.api.repositorys.LoanRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LMSApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
public class StatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoanRepository loanRepository;

    private String loanId= "123456789";


    private Loan returnLoan(){

        Loan  loan = new Loan();
        loan.setLoanId(loanId);
        loan.setPaybackDate(LocalDate.now());
        loan.setCreatedTime(LocalTime.now());
        loan.setInterest(0);
        loan.setCreatedDate(LocalDate.now());
        loan.setPaidback(false);
        loan.setPaybackAmount(1000);
        loan.setActive(true);
        loan.setCustomerNumber("08131528807");
        loan.setRequestedAmount(900);

        return loan;
    }

    @Test
    public void checkStatus_ValidLoanId_200Test(){

        try {

            Mockito.when(this.loanRepository.existsByLoanIdAndIsActiveTrue(loanId)).thenReturn(Mono.just(true));

            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/status/{loanId}", loanId))
                    .andExpect(status().isOk())
                   // .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(jsonPath("$.statusCode", Matchers.is(NotificationStatusCode.OK.getCode())))
                    .andExpect(jsonPath("$.loanId", Matchers.is(loanId)))
                    .andExpect(jsonPath("$.isActive", Matchers.is(true)))
                    .andExpect(jsonPath("$.description", Matchers.is("Loan is active")));
        }
        catch (Exception e){
            log.error("Error while checking the status of the loan {}", e);
        }
    }


    @Test
    public void checkStatus_InValid_NotFoundLoanId_404Test(){

        try {

            String notfoundLoanId = "00000";

            Mockito.when(this.loanRepository.existsByLoanIdAndIsActiveTrue(notfoundLoanId)).thenReturn(Mono.just(false));

            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/status/{loanId}", notfoundLoanId))
                    .andExpect(status().isNotFound())
                    // .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(jsonPath("$.statusCode", Matchers.is(NotificationStatusCode.SERVICE_NOT_FOUND.getCode())))
                    .andExpect(jsonPath("$.loanId", Matchers.is(notfoundLoanId)))
                    .andExpect(jsonPath("$.isActive", Matchers.is(false)))
                    .andExpect(jsonPath("$.description", Matchers.is("Loan is inactive")));
        }
        catch (Exception e){
            log.error("Error while checking the status of the loan {}", e);
        }
    }
}
