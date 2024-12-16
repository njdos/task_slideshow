package com.novisign.slideshow.task.slideshow.exception;

public class CustomDatabaseException extends RuntimeException {

    public CustomDatabaseException() {
        super();
    }

    public CustomDatabaseException(String message) {
        super(message);
    }

    public CustomDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomDatabaseException(Throwable cause) {
        super(cause);
    }
}
