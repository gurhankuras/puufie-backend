package com.kuras.learnspring.learnspring.push_notification.controller;



import com.kuras.learnspring.learnspring.push_notification.dto.RegisterPushTokenRequest;
import com.kuras.learnspring.learnspring.push_notification.service.PushTokenService;
import com.kuras.learnspring.learnspring.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/push-token")
@RequiredArgsConstructor
public class PushNotificationTokenController {

    private final PushTokenService pushTokenService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerAnonymous(@RequestBody RegisterPushTokenRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails user
    ) {
        pushTokenService.registerToken(request, user);
        return ResponseEntity.ok().build();
    }
}
