package com.kuras.learnspring.learnspring.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PasswordValidationResult {
    private boolean success;
    private List<String> errors;

    public static PasswordValidationResult fail(List<String> errors) {
        return new PasswordValidationResult(false, errors);
    }

    public static PasswordValidationResult ok() {
        return new PasswordValidationResult(true, Collections.EMPTY_LIST);
    }
}
