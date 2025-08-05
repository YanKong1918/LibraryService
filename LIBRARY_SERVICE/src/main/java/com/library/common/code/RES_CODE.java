package com.library.common.code;

public enum RES_CODE {

    SUCCESS_200(200, "성공"),
    SUCCESS(201, "등록 성공"),
    SUCCESS_204(204, "클라이언트의 요구를 처리했으나 전달할 데이터가 없음"),

    ERROR(400, "잘못된 요청입니다"),
    ERROR_404(404, "요청을 처리할 수 없습니다")
    ;

    private final int code;
    private final String message;

    RES_CODE(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
