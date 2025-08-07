package com.library.dto;

import com.library.common.code.RES_CODE;
import com.library.common.domain.BaseResponse;
import lombok.Builder;

public class ExtendLoanResDto extends BaseResponse {

    @Builder
    private ExtendLoanResDto(int resCode, String resMsg) {
        super(resCode, resMsg);
    }

    public static ExtendLoanResDto from(RES_CODE response) {
        return ExtendLoanResDto.builder()
                .resCode(response.getCode())
                .resMsg(response.getMessage())
                .build();
    }

}
