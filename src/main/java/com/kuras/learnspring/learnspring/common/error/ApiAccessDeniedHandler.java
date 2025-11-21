package com.kuras.learnspring.learnspring.common.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component

public class ApiAccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final MessageService messageService;

    public ApiAccessDeniedHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    private String traceId() { return Optional.ofNullable(MDC.get("traceId")).orElse(UUID.randomUUID().toString()); }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // ... ApiError objesini oluştur
        var wrappedEx = new ApiException(ErrorCode.UNAUTHORIZED);
        var apiError = ApiError.of(wrappedEx, ErrorType.AUTH, request.getRequestURI(), traceId(), messageService);

        // 1. Yanıtı manuel olarak yapılandır
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 2. ApiError objesini JSON string'e çevir (YANIT YAZMADAN ÖNCE)
        String jsonResponse = mapper.writeValueAsString(apiError);

        // 3. JSON dizesini response.getWriter() ile yaz
        response.getWriter().write(jsonResponse);

        // 4. flush yapmayı unutmayın
        response.getWriter().flush();
    }
}