package com.roundtable.roundtable.global.exception.errorcode;

public enum EventErrorCode implements ErrorCode {
    UNSUPPORTED_REPETITION_TYPE("지원하지 않는 반복 유형입니다.", "event-001");

    private final String message;

    private final String code;

    EventErrorCode(String message, String code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return code;
    }
}
