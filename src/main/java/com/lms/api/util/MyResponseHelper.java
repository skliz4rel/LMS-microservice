package com.lms.api.util;

import com.lms.api.constants.NotificationStatusCode;
import com.lms.api.models.error.APIError;
import com.lms.api.models.response.APIResponse;

public class MyResponseHelper {

    public static APIResponse noDataResp(String message){

        APIResponse apiResponse =new APIResponse();

        apiResponse.setStatusCode(NotificationStatusCode.SERVICE_NOT_FOUND.getCode());
        apiResponse.setStatusMessage(NotificationStatusCode.SERVICE_NOT_FOUND.getDescription());

        APIError apiError = new APIError();
        apiError.setStatus(NotificationStatusCode.SERVICE_NOT_FOUND.getHttpStatus());
        apiError.setStatusMessage(message);
        apiError.setStatusCode(NotificationStatusCode.SERVICE_NOT_FOUND.getCode());

        apiResponse.setError(apiError);

        return apiResponse;
    }

    public static APIResponse returnNoTokenresp(String message){

        APIResponse apiResponse =new APIResponse();

        apiResponse.setStatusCode(NotificationStatusCode.CLIENT_UNAUTHORIZED.getCode());
        apiResponse.setStatusMessage(NotificationStatusCode.CLIENT_UNAUTHORIZED.getDescription());

        APIError apiError = new APIError();
        apiError.setStatus(NotificationStatusCode.CLIENT_UNAUTHORIZED.getHttpStatus());
        apiError.setStatusMessage(message);
        apiError.setStatusCode(NotificationStatusCode.CLIENT_UNAUTHORIZED.getCode());

        apiResponse.setError(apiError);

        return apiResponse;
    }

}
