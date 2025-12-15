package com.kuras.learnspring.learnspring.push_notification.controller;

import com.kuras.learnspring.learnspring.common.dto.PagedResponse;
import com.kuras.learnspring.learnspring.common.utils.IpUtils;
import com.kuras.learnspring.learnspring.push_notification.dto.RegisterPushTokenRequest;
import com.kuras.learnspring.learnspring.push_notification.dto.UserNotificationDto;
import com.kuras.learnspring.learnspring.push_notification.dto.UserPushNotificationPreferenceDto;
import com.kuras.learnspring.learnspring.push_notification.entity.UserNotification;
import com.kuras.learnspring.learnspring.push_notification.mapper.UserNotificationMapper;
import com.kuras.learnspring.learnspring.push_notification.model.NotificationContext;
import com.kuras.learnspring.learnspring.push_notification.service.*;
import com.kuras.learnspring.learnspring.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/push-notification")
public class PushNotificationController {

    private final UserPushPrefService service;
    private final NotificationService notificationService;
    private final UserNotificationService userNotificationService;
    private final NotificationCommandService notificationCommandService;

    @GetMapping("/user-notification")
    public ResponseEntity<PagedResponse<UserNotificationDto>> getUserNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        var userId = user.getUserId();
        var notifications = userNotificationService.getUserNotifications(userId, page, size);
        var dtos = PagedResponse.fromPage(notifications.map(UserNotificationMapper::toDto));
        return ResponseEntity.ok(dtos);
    }


    @PostMapping("/markAllRead")
    public ResponseEntity<Void> markAllRead(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        var userId = user.getUserId();
        userNotificationService.markAllRead(userId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}/markRead")
    public ResponseEntity<Void> markRead(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        var userId = user.getUserId();
        userNotificationService.markRead(id, userId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/send")
    public ResponseEntity<Long> create(@RequestBody NotificationContext ctx) {
        var id = notificationCommandService.createAndQueue(ctx);
        return ResponseEntity.ok(id);
    }


    // CREATE  -> POST /api/users/{userId}/push-preferences
    @PostMapping("/user/{userId}/push-preferences")
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
    @GetMapping("/user/{userId}/push-preferences")
    public ResponseEntity<UserPushNotificationPreferenceDto> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    // UPDATE  -> PATCH /api/users/{userId}/push-preferences/{prefId}
    // (Partial update mantığında; DTO'da Boolean kullanıyorsan null'lar korunur)
    @PutMapping("/user/{userId}/push-preferences/{prefId}")
    public ResponseEntity<UserPushNotificationPreferenceDto> update(
            @PathVariable Long userId,   // İstersen service içinde userId kontrolü ekleyebilirsin
            @PathVariable Long prefId,
            @Valid @RequestBody UserPushNotificationPreferenceDto dto) {

        var updated = service.update(prefId, dto);
        return ResponseEntity.ok(updated);
    }
}
