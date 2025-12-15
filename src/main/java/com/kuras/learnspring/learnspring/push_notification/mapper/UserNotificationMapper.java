package com.kuras.learnspring.learnspring.push_notification.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuras.learnspring.learnspring.push_notification.dto.UserNotificationDto;
import com.kuras.learnspring.learnspring.push_notification.entity.UserNotification;

import java.util.Collections;
import java.util.Map;

public class UserNotificationMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static UserNotificationDto toDto(UserNotification n) {

        return UserNotificationDto.builder()
                .id(n.getId())
                .title(n.getTitle())
                .body(n.getBody())
                .category(n.getCategory())
                .notificationType(n.getNotificationType())
                .target(n.getTarget())
                .data(n.getData())
                .isRead(n.isRead())
                .readAt(n.getReadAt())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
