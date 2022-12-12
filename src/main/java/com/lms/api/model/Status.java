package com.lms.api.model;

import lombok.Data;

@Data
public class Status {

    private String operation;

    private String description;

    private boolean isSuccessful;
}
