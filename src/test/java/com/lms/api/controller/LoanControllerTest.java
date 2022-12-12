package com.lms.api.controller;

import com.lms.api.LMSApplication;
import com.lms.api.model.request.LoanRequest;
import com.lms.api.test.TestNumbers;
import com.lms.api.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LMSApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
class LMSApplicationTests {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void LoanRequestShouldBeSuccessful() throws Exception {

        LoanRequest loanRequest  = new LoanRequest();
        loanRequest.setAmount(1000);
        loanRequest.setCustomerNumber(TestNumbers.CustomerID_1);


        mockMvc.perform(MockMvcRequestBuilders.post(Constants.LOAN_REQUEST_URL+"/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanRequest)))
                .andExpect(status().isOk());

        /*
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.respCode", Matchers.is("00")))
                .andExpect(jsonPath("$.respDescription", Matchers.is("success")))
                .andExpect(jsonPath("$.respBody.date", Matchers.is("2021-12-20")))
                .andExpect(jsonPath("$.respBody.amount", Matchers.is("2000")))
                .andExpect(jsonPath("$.respBody.currencyCode", Matchers.is("NAIRA")))*/

    }




}