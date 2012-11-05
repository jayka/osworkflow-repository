package com.opensymphony.workflow.service.exception;

public class InvalidInputException extends WorkflowException {
    private static final long serialVersionUID = 8448380578189595210L;

    public InvalidInputException() {
    }

    public InvalidInputException(Throwable cause) {
        super(cause);
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}