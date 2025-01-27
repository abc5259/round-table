package com.roundtable.roundtable.global.response;

import lombok.Getter;

@Getter
public class SuccessResponse<T> extends ResponseDto<T> {

    private SuccessResponse() {
        super(true, null);
    }

    private SuccessResponse(T data) {
        super(true, data);
    }

    public static <T> SuccessResponse<T> from(T data) {
        return new SuccessResponse<>(data);
    }

    public static <T> SuccessResponse<Void> ok() {
        return new SuccessResponse<>();
    }
}
