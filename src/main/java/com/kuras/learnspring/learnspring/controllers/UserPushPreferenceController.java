package com.kuras.learnspring.learnspring.controllers;

import com.kuras.learnspring.learnspring.dto.UserPushNotificationPreferenceDto;
import com.kuras.learnspring.learnspring.service.UserPushPrefService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/users/{userId}/push-preferences")
public class UserPushPreferenceController {

    private final UserPushPrefService service;

    // CREATE  -> POST /api/users/{userId}/push-preferences
    @PostMapping
    public ResponseEntity<UserPushNotificationPreferenceDto> create(
            @PathVariable Long userId,
            @Valid @RequestBody UserPushNotificationPreferenceDto dto) {

        var created = service.create(userId, dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    // READ    -> GET  /api/users/{userId}/push-preferences
    @GetMapping
    public ResponseEntity<UserPushNotificationPreferenceDto> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    // UPDATE  -> PATCH /api/users/{userId}/push-preferences/{prefId}
    // (Partial update mantığında; DTO'da Boolean kullanıyorsan null'lar korunur)
    @PutMapping("/{prefId}")
    public ResponseEntity<UserPushNotificationPreferenceDto> update(
            @PathVariable Long userId,   // İstersen service içinde userId kontrolü ekleyebilirsin
            @PathVariable Long prefId,
            @Valid @RequestBody UserPushNotificationPreferenceDto dto) {

        var updated = service.update(prefId, dto);
        return ResponseEntity.ok(updated);
    }
}
