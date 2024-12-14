package com.novisign.slideshow.task.slideshow.model.responce;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ApiResponse {
    private String status;
    private int code;
    private String message;
    private List<Map<String, Object>> data;

    public ApiResponse(int code, boolean isSuccess, List<Map<String, Object>> data) {
        this.code = code;
        this.status = isSuccess ? "success" : "error";
        this.message = ErrorCodes.getErrorMessage(code);
        this.data = data;
    }

    public static ApiResponse success(int code, List<Map<String, Object>> data) {
        return new ApiResponse(code, true, data);
    }

    public static ApiResponse error(int code) {
        return new ApiResponse(code, false, Collections.emptyList());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
