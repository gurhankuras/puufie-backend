package com.kuras.learnspring.learnspring.auth.service;

import com.kuras.learnspring.learnspring.auth.dto.CreateOtpRequest;
import com.kuras.learnspring.learnspring.auth.entity.OtpCode;
import com.kuras.learnspring.learnspring.auth.entity.OtpStatus;
import com.kuras.learnspring.learnspring.auth.model.OtpValidationRequest;
import com.kuras.learnspring.learnspring.auth.repository.OtpCodeRepository;
import com.kuras.learnspring.learnspring.auth.repository.UserRepository;
import com.kuras.learnspring.learnspring.common.error.BusinessException;
import com.kuras.learnspring.learnspring.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RequiredArgsConstructor
@Service
public class OtpService {
    private final OtpCodeRepository otpCodeRepository;
    private final UserRepository userRepository;
    private final OtpAttemptService attemptService;

    private static final SecureRandom random = new SecureRandom();


    public OtpCode createOtp(CreateOtpRequest request) {
        var user = userRepository.findByUsernameOrThrow(request.getUsername());
        var code = String.format("%06d", random.nextInt(1_000_000));
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime expiryDateTime = now.plus(request.getExpirationDuration());

        OtpCode otp = OtpCode.builder()
                .user(user)
                .code(code)
                .subject(request.getSubject())
                .createdAt(now)
                .expiresAt(expiryDateTime)
                .attemptCount(0)
                .status(OtpStatus.PENDING)
                .build();

        otpCodeRepository.save(otp);

        return otp;
    }


    public void verifyOtp(OtpValidationRequest request) {
        var user = userRepository.findByUsernameOrThrow(request.getUsername());
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);


        var otp = otpCodeRepository
                .findTopByUserAndSubjectAndExpiresAtAfterAndStatusOrderByCreatedAtDesc(
                        user,
                        request.getSubject(),
                        now,
                        OtpStatus.PENDING
                )
                .orElseThrow(() -> new BusinessException(ErrorCode.OTP_CODE_FAILED_TO_VALIDATE));

        if (otp.getStatus() == OtpStatus.BLOCKED) {
            throw new BusinessException(ErrorCode.OTP_CODE_FAILED_TO_VALIDATE);
        }

        now = LocalDateTime.now(ZoneOffset.UTC);
        var notExpired = otp.getExpiresAt().isAfter(now);
        var isEqual = otp.getCode().equals(request.getCode());
        boolean ok =  isEqual && notExpired;
        if (!ok) {
            attemptService.incrementAttempts(otp.getId(), request.getMaxAttemptCount()); // REQUIRES_NEW
            throw new BusinessException(ErrorCode.OTP_CODE_FAILED_TO_VALIDATE);
        }

        attemptService.markUsed(otp.getId());
    }
}
