package com.roundtable.roundtable.global.exception;

import com.roundtable.roundtable.global.exception.errorcode.CommonErrorCode;
import com.roundtable.roundtable.global.exception.errorcode.ErrorCode;

public class CoreException extends ApplicationException {

    private final ErrorCode errorCode;

    public CoreException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static class NotFoundEntityException extends CoreException {

        public NotFoundEntityException() {
            super(CommonErrorCode.NOT_FOUND_ENTITY);
        }

        public NotFoundEntityException(ErrorCode errorCode) {
            super(errorCode);
        }
    }

    public static class CreateEntityException extends CoreException {

        public CreateEntityException() {
            super(CommonErrorCode.CRATE_ENTITY);
        }

        public CreateEntityException(ErrorCode errorCode) {
            super(errorCode);
        }
    }

    public static class DuplicatedException extends CoreException {

        public DuplicatedException() {
            super(CommonErrorCode.DUPLICATED);
        }

        public DuplicatedException(ErrorCode errorCode) {
            super(errorCode);
        }
    }
}
