package com.novisign.slideshow.task.slideshow.exception;

public class InvalidIdFormatException extends RuntimeException {

    public InvalidIdFormatException(String message) {
        super(message);
    }

    public InvalidIdFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}