package com.itmo.coursework.exception;

public class CourseworkSQLBehaviourException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Not expected behaviour of sql query";

    public CourseworkSQLBehaviourException() {
        this(DEFAULT_MESSAGE);
    }

    public CourseworkSQLBehaviourException(String message) {
        super(message);
    }

}
