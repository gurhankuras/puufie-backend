package com.kuras.learnspring.learnspring.auth.service;

import com.kuras.learnspring.learnspring.auth.dto.PasswordRequirementsDto;
import com.kuras.learnspring.learnspring.auth.model.PasswordValidationResult;
import com.kuras.learnspring.learnspring.common.error.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class PasswordValidator {
    private final MessageService messageService;

    public PasswordValidationResult validate(String rawPassword, PasswordRequirementsDto policy) {
        List<String> errors = new ArrayList<>();

        // 1) Null / boş kontrolü
        if (rawPassword == null || rawPassword.isBlank()) {
            var message = messageService.getMessage("password.empty");
            errors.add(message);
            return PasswordValidationResult.fail(errors);
        }

        // 2) Min / max length
        if (rawPassword.length() < policy.getMinLength()) {
            var message = messageService.getMessage("password.min_length", policy.getMinLength());
            errors.add(message);
        }

        if (rawPassword.length() > policy.getMaxLength()) {
            var message = messageService.getMessage("password.max_length", policy.getMaxLength());
            errors.add(message);
        }

        // 3) Karakter sınıfları
        boolean hasUpper = rawPassword.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = rawPassword.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = rawPassword.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = rawPassword.chars().anyMatch(ch ->
                !Character.isLetterOrDigit(ch)
        );

        if (policy.isRequireUpperCase() && !hasUpper) {
            var message = messageService.getMessage("password.requires_uppercase");
            errors.add(message);
        }
        if (policy.isRequireLowerCase() && !hasLower) {
            var message = messageService.getMessage("password.requires_lowercase");
            errors.add(message);
        }
        if (policy.isRequireNumber() && !hasDigit) {
            var message = messageService.getMessage("password.requires_number");
            errors.add(message);
        }
        if (policy.isRequireSpecial() && !hasSpecial) {
            var message = messageService.getMessage("password.requires_special");
            errors.add(message);
        }

        if (!errors.isEmpty()) {
            return PasswordValidationResult.fail(errors);
        }

        return PasswordValidationResult.ok();
    }
}

