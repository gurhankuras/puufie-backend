package com.kuras.learnspring.learnspring.controllers;

// com.example.security.auth.AuthController.java

import com.kuras.learnspring.learnspring.service.AuthResponse;
import com.kuras.learnspring.learnspring.service.AuthService;
import com.kuras.learnspring.learnspring.service.LoginRequest;
import com.kuras.learnspring.learnspring.service.RegisterRequest;
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
