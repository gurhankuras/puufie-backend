package com.kuras.learnspring.learnspring.error;

import java.util.Map;

public class BusinessException extends ApiException {
    public BusinessException(ErrorCode errorCode) { super(errorCode); }
    public BusinessException(ErrorCode errorCode, Map<String, Object> details) { super(errorCode, details); }
}
