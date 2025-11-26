package com.kuras.learnspring.learnspring.localization.service;

import com.kuras.learnspring.learnspring.localization.dto.LocalizationTexts;
import com.kuras.learnspring.learnspring.localization.entity.LocalizationEntry;
import com.kuras.learnspring.learnspring.localization.repository.LocalizationEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalizationService {
    private final LocalizationEntryRepository repository;

    public LocalizationTexts getLocalizationForLanguage(String language) {
        var entries = repository.findAllForLatestVersion(language);

        LocalizationTexts response;
        if (entries.isEmpty()) {
            var emptyDto = LocalizationTexts.builder()
                    .version(1)
                    .locale(language)
                    .strings(Collections.emptyMap())
                    .build();
            return emptyDto;
        }

        Integer version = entries.getFirst().getVersion();
        Map<String, String> strings = entries.stream()
                .collect(Collectors.toMap(LocalizationEntry::getKey, LocalizationEntry::getValue));

        var dto = LocalizationTexts.builder()
                .locale(language)
                .version(version)
                .strings(strings)
                .build();
        return dto;
    }
}
