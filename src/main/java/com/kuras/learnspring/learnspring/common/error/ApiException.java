package com.kuras.learnspring.learnspring.common.error;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public ApiException(ErrorCode errorCode) {
        super(Objects.requireNonNull(errorCode).code());
        this.errorCode = errorCode;
        this.details = Collections.emptyMap();
    }

    public ApiException(ErrorCode errorCode, Map<String, Object> details) {
        super(Objects.requireNonNull(errorCode).code());
        this.errorCode = errorCode;
        this.details = details == null ? Collections.emptyMap() : Map.copyOf(details);
    }

    public ErrorCode errorCode() { return errorCode; }
    public String code() { return errorCode.code(); }
    public HttpStatus status() { return errorCode.status(); }
    public Map<String, Object> details() { return details; }
}
