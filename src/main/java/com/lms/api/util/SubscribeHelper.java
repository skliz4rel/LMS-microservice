package com.lms.api.util;

import com.lms.api.constants.NotificationStatusCode;
import com.lms.api.model.request.SubscribeRequest;
import com.lms.api.models.error.APIError;
import com.lms.api.models.response.APIResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SubscribeHelper {


    public APIResponse returnSuccessResp(SubscribeRequest request) {

        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatusCode(NotificationStatusCode.OK.getCode());
        apiResponse.setStatusMessage(NotificationStatusCode.OK.getDescription());
        apiResponse.setTimeStamp(LocalDateTime.now().toString());

        apiResponse.setData(request);

        return apiResponse;
    }

    public  APIResponse returnFailedResp(SubscribeRequest request) {

        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatusCode(NotificationStatusCode.DOWNSTREAM_SERVER_ERROR.getCode());
        apiResponse.setStatusMessage(NotificationStatusCode.DOWNSTREAM_SERVER_ERROR.getDescription());
        apiResponse.setTimeStamp(LocalDateTime.now().toString());

        apiResponse.setData(request);

        APIError error = new  APIError();
        error.setStatusCode(NotificationStatusCode.DOWNSTREAM_SERVER_ERROR.getCode());
        error.setStatus(NotificationStatusCode.DOWNSTREAM_SERVER_ERROR.getHttpStatus());

        apiResponse.setData(error);

        return apiResponse;
    }
}
