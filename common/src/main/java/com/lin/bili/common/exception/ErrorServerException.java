package com.lin.bili.common.exception;

public abstract class ErrorServerException extends Exception {
    public ErrorServerException(String message) {
        super(message);
    }

    public ErrorServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorServerException(Throwable cause) {
        super(cause);
    }

    public ErrorServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
