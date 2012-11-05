package com.opensymphony.workflow.service.exception;

public class ContentProcessException extends Exception {

    private static final long serialVersionUID = -2062093770282397853L;

    public ContentProcessException() {
    }

    public ContentProcessException(Throwable cause) {
        super(cause);
    }

    public ContentProcessException(String message) {
        super(message);
    }

    public ContentProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}