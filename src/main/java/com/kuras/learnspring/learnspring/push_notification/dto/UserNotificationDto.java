package com.kuras.learnspring.learnspring.push_notification.dto;

import lombok.*;

import java.time.Instant;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationDto {

    private Long id;

    private String title;

    private String body;

    private String category;

    private String notificationType;

    private String target;

    private Map<String, Object> data;

    private boolean isRead;

    private Instant readAt;

    private Instant createdAt;
}


