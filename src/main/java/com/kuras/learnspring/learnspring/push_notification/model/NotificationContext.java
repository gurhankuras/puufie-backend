package com.kuras.learnspring.learnspring.push_notification.model;

import com.kuras.learnspring.learnspring.push_notification.constant.NotificationType;

import java.util.Map;

public record NotificationContext(
        Long userId,
        NotificationType type,
        Map<String, Object> data
) {
    public static NotificationContext of(
            Long userId,
            NotificationType type,
            Map<String, Object> data
    ) {
        return new NotificationContext(userId, type, data);
    }
}
