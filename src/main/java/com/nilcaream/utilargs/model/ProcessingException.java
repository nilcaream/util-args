package com.nilcaream.utilargs.model;

public class ProcessingException extends UtilArgsException {

    public ProcessingException(String message) {
        super(message);
    }

    public ProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
