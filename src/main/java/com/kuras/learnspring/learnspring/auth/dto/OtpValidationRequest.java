package com.kuras.learnspring.learnspring.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OtpValidationRequest {
    private String username;
    private String subject;
    private String code;
}
