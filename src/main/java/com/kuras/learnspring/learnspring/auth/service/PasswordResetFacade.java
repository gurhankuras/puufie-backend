package com.kuras.learnspring.learnspring.auth.service;

import com.kuras.learnspring.learnspring.auth.dto.OtpValidationRequestDto;
import com.kuras.learnspring.learnspring.auth.dto.PasswordResetResponse;
import com.kuras.learnspring.learnspring.auth.model.OtpValidationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// PasswordResetFacade.java
@Service
@RequiredArgsConstructor
public class PasswordResetFacade {
    private final OtpService otpService;
    private final PasswordResetService passwordResetService;

    @Transactional
    public PasswordResetResponse verifyPasswordResetOtp(OtpValidationRequestDto req) {
        var verifyRequest = OtpValidationRequest.builder()
                .code(req.getCode())
                .maxAttemptCount(3)
                .subject(req.getSubject())
                .username(req.getUsername())
                .build();

        otpService.verifyOtp(verifyRequest);
        String token = passwordResetService.createResetToken(req.getUsername());
        return new PasswordResetResponse(token);
    }
}
