package com.kuras.learnspring.learnspring.common.error;

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
        Map<String, ?> details
) {
    public static ApiError of(ApiException ex, ErrorType type, String path, String traceId, MessageService messageService) {
        Map<String, ?> details = ex.details();
        Object[] args = null;

        if (details != null && details.containsKey("args")) {
            Object val = details.get("args");
            if (val instanceof Object[]) {
                args = (Object[]) val;
            } else {
                args = new Object[]{ val };
            }
        } else {
            args = new Object[]{}; // boş args
        }

        String localizedMessage = messageService.getErrorMessage(ex.code(), args); // Details içindeki argümanlar kullanılabilir

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
