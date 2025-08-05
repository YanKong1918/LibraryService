package com.library.dto;

import com.library.common.code.RES_CODE;
import com.library.common.domain.BaseResponse;
import lombok.Builder;

public class ReturnBooksResDto extends BaseResponse {

    @Builder
    private ReturnBooksResDto(int resCode, String resMsg) {
        super(resCode, resMsg);
    }

    public static ReturnBooksResDto from(RES_CODE response) {
        return ReturnBooksResDto.builder()
                .resCode(response.getCode())
                .resMsg(response.getMessage())
                .build();
    }
}
