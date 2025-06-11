package com.test.library.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

public class FindBooksResDto {
	
	@JsonProperty("res_code")
	private int resCode;
	
	@JsonProperty("res_msg")
	private String resMsg;
	
	@JsonProperty("data")
	@JsonInclude(Include.NON_NULL)
	private Object data;
	
	@Builder
	private FindBooksResDto (int resCode, String resMsg, Object data) {
		this.resCode = resCode;
		this.resMsg = resMsg;
		this.data = data;
	}
	
	public static FindBooksResDto from(int resCode, String resMsg) {
		return FindBooksResDto.builder()
				.resCode(resCode)
				.resMsg(resMsg)
				.build();
	}
	
	public static FindBooksResDto of(int resCode, String resMsg, Object data) {
		return FindBooksResDto.builder()
				.resCode(resCode)
				.resMsg(resMsg)
				.data(data)
				.build();
	}
	

}
