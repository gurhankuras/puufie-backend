package com.kuras.learnspring.learnspring.common.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    private final HttpServletRequest request;

    /**
     * ErrorCode'dan gelen kodu çevirir.
     */
    public String getErrorMessage(String code, Object... args) {
        Locale locale = localeResolver.resolveLocale(request);

        // Hata kodları için prefix ekleyelim (örn: "error." + "USER_NOT_FOUND")
        String key = "error." + code;

        return messageSource.getMessage(key, args, locale);
    }

    public String getMessage(String key, Object... args) {
        Locale locale = localeResolver.resolveLocale(request);
        return messageSource.getMessage(key, args, locale);
    }

    /**
     * DTO validasyonu için özel mesajları çevirir.
     */
    public String getValidationMessage(String code, Object... args) {
        Locale locale = localeResolver.resolveLocale(request);
        return messageSource.getMessage(code, args, locale);
    }
}
