package com.rufus.store.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;


@Getter
public abstract class BaseApiException extends RuntimeException {
    private final HttpStatus status;
    private final String errorType;
    private final String title;

    protected BaseApiException(String message, HttpStatus status, String errorType, String title) {
        super(message);
        this.status = status;
        this.errorType = errorType;
        this.title = title;
    }

    /**
     * Override this method to add custom properties to the ProblemDetail response.
     * Default implementation does nothing.
     *
     * @param problem the ProblemDetail to customize
     */
    public void customizeResponse(ProblemDetail problem) {
        // Subclasses can override to add custom fields
    }
}
