package com.opensymphony.workflow.service.exception;

public class InvalidActionException extends InternalWorkflowException {

    private static final long serialVersionUID = 3749049026091751825L;

    public InvalidActionException() {
    }

    public InvalidActionException(Throwable cause) {
        super(cause);
    }

    public InvalidActionException(String message) {
        super(message);
    }

    public InvalidActionException(String message, Throwable cause) {
        super(message, cause);
    }
}