package com.kuras.learnspring.learnspring.push_notification.dto;

import com.kuras.learnspring.learnspring.push_notification.entity.Device;
import jakarta.persistence.Column;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPushTokenRequest {
    private String token;
    private Device.Platform platform;
    private String osVersion;
    private String appVersion;
}
