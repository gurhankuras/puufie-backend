package com.kuras.learnspring.learnspring.auth.service;

import com.kuras.learnspring.learnspring.auth.entity.OtpStatus;
import com.kuras.learnspring.learnspring.auth.repository.OtpCodeRepository;
import com.kuras.learnspring.learnspring.common.error.BusinessException;
import com.kuras.learnspring.learnspring.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

// OtpAttemptService.java
@Service
@RequiredArgsConstructor
public class OtpAttemptService {

    private final OtpCodeRepository otpRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementAttempts(Long otpId, int maxAttempts) {
        var otp = otpRepo.findByIdForUpdate(otpId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OTP_CODE_FAILED_TO_VALIDATE));

        int newCount = otp.getAttemptCount() + 1;
        otp.setAttemptCount(newCount);
        if (newCount >= maxAttempts) {
            otp.setStatus(OtpStatus.BLOCKED);
        }
        otp.setLastAttemptAt(LocalDateTime.now());
    }

    @Transactional
    public void markUsed(Long otpId) {
        var otp = otpRepo.findByIdForUpdate(otpId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OTP_CODE_FAILED_TO_VALIDATE));
        if (otp.getStatus() == OtpStatus.VERIFIED) return;
        otp.setStatus(OtpStatus.VERIFIED);
        otp.setUsedAt(LocalDateTime.now());
    }
}
