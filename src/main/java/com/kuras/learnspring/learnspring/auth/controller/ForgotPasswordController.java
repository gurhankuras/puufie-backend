package com.kuras.learnspring.learnspring.auth.controller;

import com.kuras.learnspring.learnspring.auth.dto.OtpRequest;
import com.kuras.learnspring.learnspring.auth.dto.OtpValidationRequest;
import com.kuras.learnspring.learnspring.auth.entity.OtpCode;
import com.kuras.learnspring.learnspring.auth.entity.OtpStatus;
import com.kuras.learnspring.learnspring.common.error.BusinessException;
import com.kuras.learnspring.learnspring.common.error.ErrorCode;
import com.kuras.learnspring.learnspring.auth.repository.OtpCodeRepository;
import com.kuras.learnspring.learnspring.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/api/auth/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController {
    final private OtpCodeRepository otpRepository;
    final private UserRepository userRepository;
    private static final SecureRandom random = new SecureRandom();


    @PostMapping("/request")
    public ResponseEntity<Void> requestForgotPassword(@RequestBody OtpRequest req) {
        var user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        var code = String.format("%06d", random.nextInt(1_000_000));
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime expiryDateTime = now.plusMinutes(30);

        OtpCode otp = OtpCode.builder()
                .user(user)
                .code(code)
                .subject("FORGOT_PASSWORD")
                .createdAt(now)
                .expiresAt(expiryDateTime)
                .attemptCount(0)
                .status(OtpStatus.PENDING)
                .build();

        otpRepository.save(otp);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/verify-otp")
    @Transactional(dontRollbackOn = BusinessException.class)
    public ResponseEntity<Void> verifyOtp(@RequestBody OtpValidationRequest req) {
        var user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        final int MAX_ATTEMPTS = 3;

        var otp = otpRepository
                .findTopByUserAndSubjectAndExpiresAtAfterAndStatusOrderByCreatedAtDesc(
                        user,
                        req.getSubject(),
                        now,
                        OtpStatus.PENDING
                )
                .orElseThrow(() -> new BusinessException(ErrorCode.OTP_CODE_FAILED_TO_VALIDATE));

        // 1) Daha önce çok denemiş mi? (kilitli kabul et)
        if (otp.getAttemptCount() >= MAX_ATTEMPTS || otp.getStatus() == OtpStatus.BLOCKED) {
            otp.setStatus(OtpStatus.BLOCKED);
            otpRepository.save(otp);
            throw new BusinessException(ErrorCode.OTP_TOO_MANY_ATTEMPTS);
        }

        // 2) Son deneme zamanını güncelle
        otp.setLastAttemptAt(now);

        // 3) Kod yanlışsa: attempt +1, gerekirse kilitle
        if (!otp.getCode().equals(req.getCode())) {
            otp.setAttemptCount(otp.getAttemptCount() + 1);

            if (otp.getAttemptCount() >= MAX_ATTEMPTS) {
                otp.setStatus(OtpStatus.BLOCKED);
            }

            otpRepository.save(otp);
            throw new BusinessException(ErrorCode.OTP_CODE_FAILED_TO_VALIDATE);
        }

        // 4) Kod doğruysa: doğrula, used_at + status güncelle
        otp.setUsedAt(now);
        otp.setStatus(OtpStatus.VERIFIED);
        otpRepository.save(otp);

        return ResponseEntity.noContent().build();
    }

}
