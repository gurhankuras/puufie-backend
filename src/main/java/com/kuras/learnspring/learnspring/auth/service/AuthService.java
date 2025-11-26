package com.kuras.learnspring.learnspring.auth.service;

// com.example.security.auth.AuthService.java

import com.kuras.learnspring.learnspring.auth.dto.AuthResponse;
import com.kuras.learnspring.learnspring.auth.dto.LoginRequest;
import com.kuras.learnspring.learnspring.auth.dto.SignupRequest;
import com.kuras.learnspring.learnspring.auth.entity.PasswordHistory;
import com.kuras.learnspring.learnspring.auth.entity.User;
import com.kuras.learnspring.learnspring.auth.repository.PasswordHistoryRepository;
import com.kuras.learnspring.learnspring.auth.repository.UserRepository;
import com.kuras.learnspring.learnspring.common.error.BusinessException;
import com.kuras.learnspring.learnspring.common.error.ErrorCode;
import com.kuras.learnspring.learnspring.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final com.kuras.learnspring.learnspring.security.JwtService jwtService;
    private final PasswordPolicyService passwordPolicyService;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final UserSecurityService userSecurityService;

    @Transactional
    public AuthResponse register(SignupRequest req) {
        var exists = userRepository.existsByUsername(req.username());
        if (exists) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }

        var currentPolicy = passwordPolicyService.getLatestPasswordPolicy();
        passwordPolicyService.checkPassword(req.password(), currentPolicy);

        String passwordHash = passwordEncoder.encode(req.password());
        User user = User.builder()
                .username(req.username())
                .email(req.email())
                .firstName(req.firstName())
                .lastName(req.lastName())
                .phoneNumber(req.phoneNumber())
                .countryCode(req.countryCode())
                .isActive(true)

                .build();

        userRepository.save(user);

        var passwordCreatedAt = LocalDateTime.now();
        userSecurityService.createInitialSecurityFor(user, passwordHash);

        PasswordHistory passwordHistoryEntry = PasswordHistory.builder()
                .policyVersion(currentPolicy.getVersion())
                .user(user)
                .password_hash(passwordHash)
                .createdAt(passwordCreatedAt)
                .build();

        passwordHistoryRepository.save(passwordHistoryEntry);


        String token = jwtService.generateToken(user.getUsername(), Map.of());
        return new AuthResponse(token);
    }

    @Transactional
    public AuthResponse login(LoginRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username(), req.password())
            );

            userSecurityService.onSuccessfulLogin(req.username());

            var principal = (CustomUserDetails) auth.getPrincipal();
            var roles = principal.getRoles();

            String token = jwtService.generateToken(principal.getUsername(), Map.of("roles", roles));
            return new AuthResponse(token);

        } catch (BadCredentialsException ex) {
            boolean locked = userSecurityService.onFailedLogin(req.username());

            if (locked) {
                var policy = passwordPolicyService.getLatestPasswordPolicy();
                // burada artık transaction commit olmuş durumda
                throw new BusinessException(ErrorCode.YOUR_ACCOUNT_LOCKED_OUT, policy.getLockoutDurationMinutes());
            }
            throw ex;
        }
    }

}
