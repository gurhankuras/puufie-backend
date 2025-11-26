package com.kuras.learnspring.learnspring.auth.service;

import com.kuras.learnspring.learnspring.auth.entity.PasswordHistory;
import com.kuras.learnspring.learnspring.auth.entity.PasswordResetToken;
import com.kuras.learnspring.learnspring.auth.entity.User;
import com.kuras.learnspring.learnspring.auth.repository.PasswordHistoryRepository;
import com.kuras.learnspring.learnspring.auth.repository.PasswordResetTokenRepository;
import com.kuras.learnspring.learnspring.auth.repository.UserRepository;
import com.kuras.learnspring.learnspring.auth.repository.UserSecurityRepository;
import com.kuras.learnspring.learnspring.common.error.BusinessException;
import com.kuras.learnspring.learnspring.common.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final SecureRandom secureRandom = new SecureRandom();
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordPolicyService passwordPolicyService;
    private final UserSecurityRepository userSecurityRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;

    private String generateRawToken() {
        byte[] bytes = new byte[32]; // 256-bit
        secureRandom.nextBytes(bytes);
        // URL-safe token
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not supported", e);
        }
    }

    public String createResetToken(String username) {
        User user = userRepository.findByUsernameOrThrow(username);

        String rawToken = generateRawToken();
        String tokenHash = hashToken(rawToken);

        var now = LocalDateTime.now();
        var expiryDate = now.plusMinutes(30);
        var resetToken = PasswordResetToken.builder()
                .user(user)
                .tokenHash(tokenHash)
                .createdAt(now)
                .expiresAt(expiryDate)
                .used(false)
                .build();

        passwordResetTokenRepository.save(resetToken);
        System.out.println("reset token = " + rawToken);
        return rawToken;
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        // Get current password policy and check if it obeys the rules
        var currentPolicy = passwordPolicyService.getLatestPasswordPolicy();
        passwordPolicyService.checkPassword(newPassword, currentPolicy);

        String tokenHash = hashToken(rawToken);

        // 1) Token'ı tek SQL ile sahiplen
        int updated = passwordResetTokenRepository.markTokenUsedIfValid(tokenHash);
        if (updated != 1) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD_RESET_TOKEN);
        }

        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PASSWORD_RESET_TOKEN));
        User user = resetToken.getUser();

        var newHashedPassword = passwordEncoder.encode(newPassword);

        // Update user security
        var userSecurity = userSecurityRepository.findByIdOrThrow(user.getId());

        var now = LocalDateTime.now();
        userSecurity.setPasswordHash(newHashedPassword);
        userSecurity.setPasswordLastChangedAt(now);
        userSecurity.setMustChangePassword(false);
        userSecurity.setUpdatedAt(now);
        userSecurity.setPasswordPolicyVersionAtSet(currentPolicy.getVersion());
        userSecurityRepository.save(userSecurity);

        var passwordHistoryEntry = PasswordHistory.builder()
                .password_hash(newHashedPassword)
                .policyVersion(currentPolicy.getVersion())
                .createdAt(now)
                .user(user)
                .build();
        passwordHistoryRepository.save(passwordHistoryEntry);
    }


}