package com.kuras.learnspring.learnspring.common.error;

import java.util.Map;

public class BusinessException extends ApiException {
    public BusinessException(ErrorCode errorCode) { super(errorCode); }

    public BusinessException(ErrorCode errorCode, Map<String, ?> details) { super(errorCode, details); }

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode, Map.of("args", args));
    }

    public BusinessException(ErrorCode errorCode, Map<String, ?> details, Object... args) {
        super(errorCode, merge(details, args));
    }

    private static Map<String, Object> merge(Map<String, ?> details, Object... args) {
        Map<String, Object> map = new java.util.HashMap<>(details);
        map.put("args", args);
        return map;
    }
}
