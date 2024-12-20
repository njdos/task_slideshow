package com.novisign.slideshow.task.slideshow.constant;

public enum StatusCodes {

    // 2xx: Success codes
    OK(200, "Ok"),
    SUCCESS(201, "Successfully processed operation"),

    // 1xxx: Validation errors
    FAILED_VALIDATION(1000, "Failed validation"),
    FAILED_VALIDATION_IMAGE(1001, "Failed validation image type or content"),
    FAILED_VALIDATION_DURATION(1002, "Failed validation duration of image"),

    // 2xxx: Not Found errors
    SLIDESHOW_NOT_FOUND(2000, "Slideshow Not Found"),

    // 3xxx: Validation failure errors
    VALIDATION_FAILED(3000, "Validation Failed"),

    // 4xxx: Request errors (already exists, invalid body, invalid parameters)
    ALREADY_EXISTS(4000, "Already exists"),
    INVALID_REQUEST_BODY(4001, "Invalid request body format"),
    INVALID_REQUEST_PARAMETERS(4002, "Invalid request parameters format"),

    // 5xxx: Internal server errors
    INTERNAL_SERVER_ERROR(5000, "Internal Server Error"),
    DATABASE_OPERATION_FAILED(5001, "Database Operation Failed"),
    LOADING_FILE_FAILED(5002, "Error loading file"),

    // 6xxx: Data not found / Serialization errors
    NOT_FOUND(6001, "Image not found"),
    MAPPING_ERROR(6002, "Error occurred while mapping data"),
    SERIALIZATION_ERROR(6003, "JSON serialization error occurred");

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
