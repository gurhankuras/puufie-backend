package com.kuras.learnspring.learnspring.auth.dto;

import lombok.*;

import java.time.Duration;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOtpRequest {
    private String subject;
    private String username;
    private Duration expirationDuration;

}
