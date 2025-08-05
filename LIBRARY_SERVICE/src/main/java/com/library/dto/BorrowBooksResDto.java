package com.library.dto;

import com.library.common.code.RES_CODE;
import com.library.common.domain.BaseResponse;
import lombok.Builder;

public class BorrowBooksResDto extends BaseResponse {

    @Builder
    private BorrowBooksResDto(int resCode, String resMsg) {
        super(resCode, resMsg);
    }

    public static BorrowBooksResDto from(RES_CODE response) {
        return BorrowBooksResDto.builder()
                .resCode(response.getCode())
                .resMsg(response.getMessage())
                .build();
    }

}
