package com.lms.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class StatusResponse {

    private String statusCode;

    private String loanId;

    private boolean isActive;

    private String description;
}
