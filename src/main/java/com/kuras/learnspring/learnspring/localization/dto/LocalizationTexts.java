package com.kuras.learnspring.learnspring.localization.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocalizationTexts {
    private Integer version;
    private String locale;
    private Map<String, String> strings;
}