package com.itmo.coursework.exception;

public class CourseworkIllegalArgumentException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Illegal argument while executing request";

    public CourseworkIllegalArgumentException() {
        super(DEFAULT_MESSAGE);
    }

}
