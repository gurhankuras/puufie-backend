package com.kuras.learnspring.learnspring.auth.controller;

// com.example.security.auth.AuthController.java

import com.kuras.learnspring.learnspring.auth.dto.AuthResponse;
import com.kuras.learnspring.learnspring.auth.service.AuthService;
import com.kuras.learnspring.learnspring.auth.dto.LoginRequest;
import com.kuras.learnspring.learnspring.auth.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
