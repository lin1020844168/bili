package com.lin.bili.common.exception;

public abstract class WarnServerException extends RuntimeException  {

    public WarnServerException(String message) {
        super(message);
    }

    public WarnServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public WarnServerException(Throwable cause) {
        super(cause);
    }

    public WarnServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
