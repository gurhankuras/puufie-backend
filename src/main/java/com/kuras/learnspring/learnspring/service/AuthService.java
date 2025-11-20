package com.kuras.learnspring.learnspring.service;

// com.example.security.auth.AuthService.java

import com.kuras.learnspring.learnspring.entity.User;
import com.kuras.learnspring.learnspring.error.BusinessException;
import com.kuras.learnspring.learnspring.error.ErrorCode;
import com.kuras.learnspring.learnspring.repository.UserRepository;
import com.kuras.learnspring.learnspring.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final com.kuras.learnspring.learnspring.security.JwtService jwtService;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.findByUsername(req.username()).isPresent()) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User user = User.builder()
                .username(req.username())
                .password(passwordEncoder.encode(req.password()))
                .isActive(true)
                .build();
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(), Map.of());
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );
        var principal = (CustomUserDetails) auth.getPrincipal();
        var roles = principal.getRoles();

        String token = jwtService.generateToken(principal.getUsername(), Map.of("roles", roles));
        return new AuthResponse(token);
    }
}
