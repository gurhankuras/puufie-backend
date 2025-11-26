package com.kuras.learnspring.learnspring.auth.service;

import com.kuras.learnspring.learnspring.auth.dto.PasswordPolicyDto;
import com.kuras.learnspring.learnspring.auth.entity.User;
import com.kuras.learnspring.learnspring.auth.entity.UserSecurity;
import com.kuras.learnspring.learnspring.auth.repository.UserRepository;
import com.kuras.learnspring.learnspring.auth.repository.UserSecurityRepository;
import com.kuras.learnspring.learnspring.common.error.BusinessException;
import com.kuras.learnspring.learnspring.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserSecurityService {

    private final UserRepository userRepository;
    private final UserSecurityRepository userSecurityRepository;
    private final PasswordPolicyService passwordPolicyService;

    public UserSecurity createInitialSecurityFor(User user, String passwordHash) {

        PasswordPolicyDto currentPolicy = passwordPolicyService.getLatestPasswordPolicy();
        LocalDateTime now = LocalDateTime.now();

        UserSecurity security = UserSecurity.builder()
                .user(user)
                .passwordHash(passwordHash)
                .passwordPolicyVersionAtSet(currentPolicy.getVersion())
                .passwordLastChangedAt(now)
                .mustChangePassword(false)
                .failedLoginAttempts(0)
                .lockedUntil(null)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return userSecurityRepository.save(security);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean onFailedLogin(String username) {
        var user = userRepository.findByUsernameOrThrow(username);
        var security = userSecurityRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_SECURITY_INFO_NOT_FOUND));

        var policy = passwordPolicyService.getLatestPasswordPolicy();
        Integer maxAttempts = policy.getMaxFailedAttempts();        // ör: 5
        Integer lockMinutes = policy.getLockoutDurationMinutes();      // ör: 15

        int newAttempts = security.getFailedLoginAttempts() + 1;
        security.setFailedLoginAttempts(newAttempts);

        boolean lockedNow = false;

        if (maxAttempts != null && lockMinutes != null && newAttempts >= maxAttempts) {
            security.setLockedUntil(LocalDateTime.now().plusMinutes(lockMinutes));
            lockedNow = true;
            // istersen burada log at: "User locked"
        }

        security.setUpdatedAt(LocalDateTime.now());
        userSecurityRepository.save(security);
        return lockedNow;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onSuccessfulLogin(String username) {
        var user = userRepository.findByUsernameOrThrow(username);
        var security = userSecurityRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_SECURITY_INFO_NOT_FOUND));

        security.setFailedLoginAttempts(0);
        security.setLockedUntil(null);
        security.setUpdatedAt(LocalDateTime.now());
    }

    public UserSecurity getById(long userId) {
        var security = userSecurityRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_SECURITY_INFO_NOT_FOUND));
        return security;
    }
}
