package com.library.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.library.common.code.ERROR_CODE;
import com.library.common.domain.ApiResDto;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResDto<Void> UserNotFound(UserNotFoundException ex) {
		return ApiResDto.fail(ERROR_CODE.INVALID_PARAMETER, "사용자를 확인할 수 없습니다.");
	}

	@ExceptionHandler(LoanNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResDto<Void> LoanNotFound(LoanNotFoundException ex) {
		return ApiResDto.fail(ERROR_CODE.INVALID_PARAMETER, "잘못된 접근입니다.");
	}

	@ExceptionHandler(UnavailableBookException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResDto<Void> LoanNotFound(UnavailableBookException ex) {
		return ApiResDto.fail(ERROR_CODE.BAD_REQUEST, "요청을 처리할 수 없습니다.");
	}

}
