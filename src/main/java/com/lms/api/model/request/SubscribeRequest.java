package com.lms.api.model.request;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class SubscribeRequest {

    @NotNull(message = "Customer Number can not be empty")
    @NotEmpty(message = "Customer Number can not be empty")
    private String customerNumber;
}
