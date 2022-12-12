package com.lms.api.model.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class StatusRequest {

    @NotNull(message = "Loan Id can not be empty !")
    @NotEmpty(message = "Loan Id can not be empty !")
    private String loanId;
}
