package com.kuras.learnspring.learnspring.common.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // Business
    USER_NOT_FOUND("USER_NOT_FOUND", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", HttpStatus.BAD_REQUEST),

    OTP_CODE_FAILED_TO_VALIDATE("OTP_CODE_FAILED_TO_VALIDATE", HttpStatus.BAD_REQUEST),
    OTP_TOO_MANY_ATTEMPTS("OTP_TOO_MANY_ATTEMPTS", HttpStatus.TOO_MANY_REQUESTS),

    PLATFORM_VERSION_CONFIG_NOT_FOUND("PLATFORM_VERSION_CONFIG_NOT_FOUND", HttpStatus.TOO_MANY_REQUESTS),

    // Http
    VALIDATION_FAILED("VALIDATION_FAILED", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("FORBIDDEN", HttpStatus.FORBIDDEN),
    METHOD_ARGUMENT_TYPE_MISMATCH("METHOD_ARGUMENT_TYPE_MISMATCH", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND("RESOURCE_NOT_FOUND",  HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED);

    private final String code;
    private final HttpStatus status;

    ErrorCode(String code, HttpStatus status) {
        this.code = code;
        this.status = status;
    }

    public String code() { return code; }
    public HttpStatus status() { return status; }
}
