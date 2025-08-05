package com.library.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResponse {

    @JsonProperty("res_code")
    private int resCode;

    @JsonProperty("res_msg")
    private String resMsg;


    public BaseResponse(int resCode, String resMsg) {
        this.resCode = resCode;
        this.resMsg = resMsg;
    }

}
