package com.kuras.learnspring.learnspring.localization.controller;

import com.kuras.learnspring.learnspring.localization.dto.LocalizationTexts;
import com.kuras.learnspring.learnspring.localization.service.LocalizationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/localization")
public class LocalizationController {
    private final LocaleResolver localeResolver;
    private final HttpServletRequest request;
    private final LocalizationService service;

    @GetMapping
    public ResponseEntity<LocalizationTexts> getLocalization(@RequestParam(required = false) String language) {
        String lang;
        if (language == null) {
            Locale locale = localeResolver.resolveLocale(request);
            lang= locale.getLanguage();
        } else {
            lang = language;
        }
        var dto = service.getLocalizationForLanguage(lang);
        return ResponseEntity.ok(dto);
    }
}
