package com.kuras.learnspring.learnspring.error;

import com.kuras.learnspring.learnspring.error.ApiException;
import com.kuras.learnspring.learnspring.error.ErrorCode;

import java.util.List;
import java.util.Map;

public class ValidationException extends ApiException {

    public record Issue(String field, String code, String message, Map<String, Object> args) {}

    public ValidationException(ErrorCode errorCode, List<Issue> issues) {
        super(errorCode, Map.of(
                "validation.issues", issues == null ? List.of() : List.copyOf(issues)
        ));
    }
}
