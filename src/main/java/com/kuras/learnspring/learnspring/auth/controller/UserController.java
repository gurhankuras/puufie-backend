package com.kuras.learnspring.learnspring.auth.controller;
import com.kuras.learnspring.learnspring.auth.dto.*;
import com.kuras.learnspring.learnspring.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        var users = userRepository.findAll();
        var dtos = users.stream()
                .map((x) -> UserDto.from(x))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
