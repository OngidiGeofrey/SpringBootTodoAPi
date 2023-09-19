package com.digitalvisionea.profiletasks;

public class CustomException extends RuntimeException {
    private final int errorCode;

    public CustomException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}


