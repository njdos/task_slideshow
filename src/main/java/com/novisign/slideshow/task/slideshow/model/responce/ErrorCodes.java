package com.novisign.slideshow.task.slideshow.model.responce;

import java.util.Map;

public class ErrorCodes {

    private static final Map<Integer, String> ERROR_CODES_MAP = Map.of(
            201, "Successfully added",
            1000, "Invalid Image URL",
            1001, "Unsupported Image Format",
            1002, "Invalid duration of image",
            2000, "Slideshow Not Found",
            3000, "Validation Failed",
            4000, "Already exists",
            5000, "Internal Server Error",
            5001, "Database Operation Failed"
    );

    public static String getErrorMessage(int code) {
        return ERROR_CODES_MAP.getOrDefault(code, "Unknown Error");
    }

}
