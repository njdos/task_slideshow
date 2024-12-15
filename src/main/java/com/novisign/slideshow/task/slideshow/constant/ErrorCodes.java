package com.novisign.slideshow.task.slideshow.constant;

public enum ErrorCodes {

    OK(200, "Ok"),
    SUCCESS(201, "Successfully added"),
    FAILED_VALIDATION(1000, "Failed validation"),
    FAILED_VALIDATION_IMAGE(1001, "Failed validation image type or content"),
    FAILED_VALIDATION_DURATION(1002, "Failed validation duration of image"),
    SLIDESHOW_NOT_FOUND(2000, "Slideshow Not Found"),
    VALIDATION_FAILED(3000, "Validation Failed"),
    ALREADY_EXISTS(4000, "Already exists"),
    INVALID_REQUEST(4001, "Invalid request format"),
    INTERNAL_SERVER_ERROR(5000, "Internal Server Error"),
    DATABASE_OPERATION_FAILED(5001, "Database Operation Failed");

    private final int code;
    private final String message;

    ErrorCodes(int code, String message) {
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
