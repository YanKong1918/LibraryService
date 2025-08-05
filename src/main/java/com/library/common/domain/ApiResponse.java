package com.library.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.common.code.ERROR_CODE;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

	@JsonProperty("status")
	private final String status;

	@JsonProperty("data")
	private final T data;

	// 에러코드
	@JsonProperty("code")
	private final String code;

	// 에러메세지
	@JsonProperty("message")
	private final String message;
	
	
	// 성공
	public static <T> ApiResponse<T> success(T data) {
		return ApiResponse.<T>builder()
				.status("성공").data(data)
				.build();
	}
	
	public static <T> ApiResponse<T> success() {
		return ApiResponse.<T>builder()
				.status("성공").build();
	}
	
	// 실패
	public static <T> ApiResponse<T> fail(ERROR_CODE code) {
		return ApiResponse.<T>builder()
				.status("실패").code(String.valueOf(code))
				.build();
	}
	
	public static <T> ApiResponse<T> fail(ERROR_CODE code, String message) {
		return ApiResponse.<T>builder()
				.status("실패").code(String.valueOf(code)).message(message)
				.build();
	}

}
