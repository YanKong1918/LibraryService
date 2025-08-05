package com.library.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.common.code.RES_CODE;
import com.library.common.domain.BaseResponse;
import lombok.Builder;

public class UsersLoanRecordDto extends BaseResponse {

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("data")
    private Object data;

    @Builder
    private UsersLoanRecordDto(int resCode, String resMsg, Object data) {
        super(resCode, resMsg);
        this.data = data;
    }

    public static UsersLoanRecordDto from(RES_CODE response) {
        return UsersLoanRecordDto.builder()
                .resCode(response.getCode())
                .resMsg(response.getMessage())
                .build();
    }

    public static UsersLoanRecordDto of(RES_CODE response, Object data) {
        return UsersLoanRecordDto.builder()
                .resCode(response.getCode())
                .resMsg(response.getMessage())
                .data(data)
                .build();
    }

}
