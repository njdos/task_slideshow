package com.novisign.slideshow.task.slideshow.constant;

public enum StatusCodes {

    OK(200, "Ok"),
    SUCCESS(201, "Successfully processed operation"),
    FAILED_VALIDATION(1000, "Failed validation"),
    FAILED_VALIDATION_IMAGE(1001, "Failed validation image type or content"),
    FAILED_VALIDATION_DURATION(1002, "Failed validation duration of image"),
    SLIDESHOW_NOT_FOUND(2000, "Slideshow Not Found"),
    VALIDATION_FAILED(3000, "Validation Failed"),
    ALREADY_EXISTS(4000, "Already exists"),
    INVALID_REQUEST_BODY(4001, "Invalid request body format"),
    INVALID_REQUEST_PARAMETERS(4002, "Invalid request parameters format"),
    INTERNAL_SERVER_ERROR(5000, "Internal Server Error"),
    DATABASE_OPERATION_FAILED(5001, "Database Operation Failed"),
    LOADING_FILE_FAILED(5002, "Error loading file"),
    NOT_FOUND(6001, "Image not found"),
    MAPPING_ERROR(6002, "Error occurred while mapping data");

    private final int code;
    private final String message;

    StatusCodes(int code, String message) {
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
