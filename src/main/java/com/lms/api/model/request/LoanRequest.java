package com.lms.api.model.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LoanRequest {

    @NotNull(message = "customer Number can not be empty !")
    @NotEmpty(message = "customer Number can not be empty !")
    private String customerNumber;

    @NotNull(message = "amount can not be empty !")
    private double amount;
}
