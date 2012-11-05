package com.opensymphony.workflow.service.exception;

public class InternalWorkflowException extends RuntimeException {

    private static final long serialVersionUID = 6232960195825670638L;

    public InternalWorkflowException() {
    }

    public InternalWorkflowException(Throwable cause) {
        super(cause);
    }

    public InternalWorkflowException(String message) {
        super(message);
    }

    public InternalWorkflowException(String message, Throwable cause) {
        super(message, cause);
    }
}