package com.kuras.learnspring.learnspring.auth.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpValidationRequest {
    private String username;
    private String subject;
    private String code;
    private int maxAttemptCount;
}
