package com.novisign.slideshow.task.slideshow.model;

import com.novisign.slideshow.task.slideshow.constant.StatusCodes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ApiResponse {
    private String status;
    private int code;
    private String message;
    private List<Map<String, Object>> data;

    public ApiResponse(StatusCodes errorCode, List<Map<String, Object>> data) {
        this.code = errorCode.getCode();
        this.status = errorCode == StatusCodes.SUCCESS ? "success" : "error";
        this.message = errorCode.getMessage();
        this.data = data;
    }

    public static ApiResponse success(StatusCodes statusCodes, List<Map<String, Object>> data) {
        return new ApiResponse(statusCodes, data);
    }

    public static ApiResponse error(StatusCodes errorCode) {
        return new ApiResponse(errorCode, Collections.emptyList());
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
