package com.kuras.learnspring.learnspring.auth.controller;

import com.kuras.learnspring.learnspring.auth.dto.*;
import com.kuras.learnspring.learnspring.auth.model.OtpValidationRequest;
import com.kuras.learnspring.learnspring.auth.service.OtpService;
import com.kuras.learnspring.learnspring.auth.service.PasswordResetFacade;
import com.kuras.learnspring.learnspring.auth.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth/forgot-password")
@RequiredArgsConstructor
public class PasswordResetController {
    final private OtpService otpService;
    final private PasswordResetService passwordResetService;
    final private PasswordResetFacade passwordResetFacade;

    @PostMapping("/request")
    public ResponseEntity<Void> requestForgotPassword(@RequestBody OtpRequest req) {
        var createOtpRequest = CreateOtpRequest.builder()
                .expirationDuration(Duration.ofMinutes(30))
                .username(req.getUsername())
                .subject("FORGOT_PASSWORD")
                .build();
        otpService.createOtp(createOtpRequest);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<PasswordResetResponse> verifyOtp(@RequestBody OtpValidationRequestDto req) {
        var responseDto = passwordResetFacade.verifyPasswordResetOtp(req);
        return ResponseEntity.ok(responseDto);
    }


    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetRequest req) {
        passwordResetService.resetPassword(req.getToken(), req.getNewPassword());
        return ResponseEntity.noContent().build();
    }





}
