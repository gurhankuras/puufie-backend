package com.kuras.learnspring.learnspring.error;

public enum ErrorType {
    VALIDATION,
    BUSINESS,
    AUTH,         // 401
    FORBIDDEN,    // 403
    NOT_FOUND,    // 404
    CONFLICT,     // 409
    RATE_LIMIT,   // 429
    INTEGRATION,  // 502/503/504
    METHOD_NOT_ALLOWED,
    INTERNAL
}