package com.vw.exceptions;

public class ExecutiveException extends RuntimeException{

    public ExecutiveException(String message) {
        super(message);
    }

    public ExecutiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
