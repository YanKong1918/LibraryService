package com.library.dto;

import com.library.common.code.RES_CODE;
import com.library.common.domain.BaseResponse;
import lombok.Builder;

public class FindBooksResDto extends BaseResponse {

    private Object data;

    @Builder
    private FindBooksResDto(int resCode, String resMsg, Object data) {
        super(resCode, resMsg);
        this.data = data;
    }

    public static FindBooksResDto from(RES_CODE response){
        return FindBooksResDto.builder()
                .resCode(response.getCode())
                .resMsg(response.getMessage())
                .build();
    }

    public static FindBooksResDto of(RES_CODE response, Object data){
        return FindBooksResDto.builder()
                .resCode(response.getCode())
                .resMsg(response.getMessage())
                .data(data)
                .build();
    }

}
