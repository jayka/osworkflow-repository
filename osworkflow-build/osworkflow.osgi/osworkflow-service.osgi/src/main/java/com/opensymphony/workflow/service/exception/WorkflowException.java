package com.opensymphony.workflow.service.exception;

public class WorkflowException extends Exception {

    private static final long serialVersionUID = -2634388797429125306L;

    public WorkflowException() {
    }

    public WorkflowException(Throwable cause) {
        super(cause);
    }

    public WorkflowException(String message) {
        super(message);
    }

    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
    }
}