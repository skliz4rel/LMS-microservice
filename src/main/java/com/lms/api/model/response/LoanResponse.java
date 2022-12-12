package com.lms.api.model.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoanResponse {

    private String customerNumber;

    private String LoanId;

    private String issuedDate;

    private String paybackDate;

    private double amount;

    private double interest;

    private double paybackAmount;

    private boolean isActive;

    private String comment;
}
