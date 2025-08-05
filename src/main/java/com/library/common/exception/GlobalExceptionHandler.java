package com.library.common.exception;

import com.library.common.code.RES_CODE;
import com.library.common.domain.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse> UserNotFound(UserNotFoundException e) {
        BaseResponse response = new BaseResponse(RES_CODE.ERROR_404.getCode(), "사용자를 확인할 수 없습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<BaseResponse> LoanNotFound(LoanNotFoundException e) {
        BaseResponse response = new BaseResponse(RES_CODE.ERROR_404.getCode(), "잘못된 접근입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UnavailableBookException.class)
    public ResponseEntity<BaseResponse> LoanNotFound(UnavailableBookException e) {
        BaseResponse response = new BaseResponse(RES_CODE.ERROR.getCode(), "요청을 처리할 수 없습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<BaseResponse> handleBookNotFound(BookNotFoundException e) {
        BaseResponse response = new BaseResponse(RES_CODE.ERROR_404.getCode(), "도서를 찾을 수 없습니다.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
