package com.kuras.learnspring.learnspring.common.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageService messageService;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessException ex, HttpServletRequest req) {
        var body = ApiError.of(ex, ErrorType.BUSINESS,  req.getRequestURI(), traceId(), messageService);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidation(ValidationException ex, HttpServletRequest req) {
        var body = ApiError.of(ex, ErrorType.VALIDATION, req.getRequestURI(), traceId(), messageService);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {

        var wrapped = new ApiException(ErrorCode.INTERNAL_ERROR);
        var body = ApiError.of(wrapped, ErrorType.INTERNAL, req.getRequestURI(), traceId(), messageService);
        // prod: log.error("Unhandled", ex);
        return ResponseEntity.status(wrapped.status()).body(body);
    }

    private String traceId() {
        return Optional.ofNullable(MDC.get("traceId")).orElse(UUID.randomUUID().toString());
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest req) {

        var wrapped = new ApiException(ErrorCode.NOT_FOUND); // NOT_FOUND ErrorCode'unu kullanın
        var body = ApiError.of(wrapped, ErrorType.NOT_FOUND, req.getRequestURI(), traceId(), messageService);

        return ResponseEntity.status(wrapped.status()).body(body);
    }



    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        var wrapped = new ApiException(ErrorCode.METHOD_NOT_ALLOWED);
        var body = ApiError.of(wrapped,  ErrorType.METHOD_NOT_ALLOWED, request.getRequestURI(), traceId(), messageService);

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {

        var wrapped =  new ApiException(ErrorCode.VALIDATION_FAILED,
                Map.of("reason", "Malformed JSON request body"));
        var body = ApiError.of(wrapped, ErrorType.VALIDATION, req.getRequestURI(), traceId(), messageService);

        return ResponseEntity.badRequest().body(body);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        Map<String, Object> details = Map.of(
                "field", ex.getName(),
                "requiredType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
        );
        var wrapped = new ApiException(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH, details);
        var body = ApiError.of(wrapped, ErrorType.VALIDATION, req.getRequestURI(), traceId(), messageService);

        return ResponseEntity.badRequest().body(body);
    }
}
