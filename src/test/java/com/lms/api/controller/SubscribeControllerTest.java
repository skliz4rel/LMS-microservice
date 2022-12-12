package com.lms.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.api.LMSApplication;
import com.lms.api.model.request.SubscribeRequest;
import com.lms.api.test.TestNumbers;
import com.lms.api.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LMSApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
public class SubscribeControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

   @Test
    public void subscribe(){

       try {
           SubscribeRequest request = new SubscribeRequest();
           request.setCustomerNumber(TestNumbers.CustomerID_2);

           mockMvc.perform(MockMvcRequestBuilders.post(Constants.SUBSCRIPTION_URL + "/")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(objectMapper.writeValueAsString(request)))
                   .andExpect(status().isOk());
       }
       catch (Exception e){
           log.error("Error when subscribing the customer {}",e);
       }
   }

}
