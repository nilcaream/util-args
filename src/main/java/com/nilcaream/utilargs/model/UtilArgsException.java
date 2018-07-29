package com.nilcaream.utilargs.model;

public abstract class UtilArgsException extends RuntimeException {

    public UtilArgsException(String message) {
        super(message);
    }

    public UtilArgsException(String message, Throwable cause) {
        super(message, cause);
    }
}
