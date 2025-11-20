package com.kuras.learnspring.learnspring.error;

import com.kuras.learnspring.learnspring.error.ApiException;
import com.kuras.learnspring.learnspring.error.ErrorType;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        Instant timestamp,
        int status,
        String path,
        String traceId,
        ErrorType type,
        String code,
        String message,
        Map<String, Object> details
) {
    public static ApiError of(ApiException ex, ErrorType type, String path, String traceId, MessageService messageService) {
        String localizedMessage = messageService.getMessage(ex.code(), ex.details()); // Details içindeki argümanlar kullanılabilir
        return new ApiError(
                Instant.now(),
                ex.status().value(),
                path,
                traceId,
                type,   // status’a göre type belirleniyor
                ex.code(),
                localizedMessage,
                ex.details()
        );
    }
}
