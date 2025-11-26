package com.kuras.learnspring.learnspring.auth.controller;

// com.example.security.auth.AuthController.java

import com.kuras.learnspring.learnspring.auth.dto.*;
import com.kuras.learnspring.learnspring.auth.repository.UserRepository;
import com.kuras.learnspring.learnspring.auth.service.AuthService;
import com.kuras.learnspring.learnspring.auth.service.PasswordPolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordPolicyService passwordPolicyService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid SignupRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @GetMapping("/password-policy/requirements/latest")
    public ResponseEntity<PasswordRequirementsDto> getLatestPasswordPolicy() {
        return ResponseEntity.ok(passwordPolicyService.getLatestPasswordRequirements());
    }
}
