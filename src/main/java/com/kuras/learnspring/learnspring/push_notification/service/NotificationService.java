package com.kuras.learnspring.learnspring.push_notification.service;

import com.kuras.learnspring.learnspring.push_notification.constant.NotificationType;
import com.kuras.learnspring.learnspring.push_notification.factory.UserNotificationFactory;
import com.kuras.learnspring.learnspring.push_notification.model.NotificationContext;
import com.kuras.learnspring.learnspring.push_notification.repository.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserNotificationFactory notificationFactory;
    private final UserNotificationRepository notificationRepository;
    private final PushTokenService pushTokenService;

    public void notifyPasswordChanged(Long userId, String ip, String deviceName) {
        var ctx = NotificationContext.of(
                userId,
                NotificationType.PASSWORD_CHANGED,
                Map.of(
                        "ip", ip,
                        "device", deviceName
                )
        );

        // 1) UserNotification oluştur & kaydet
        var notif = notificationFactory.create(ctx);
        notif = notificationRepository.save(notif);

        // 2) Push gönder (kullanıcıya bağlı cihazlara)
        // pushTokenService.sendPasswordChanged(userId, notif);
    }
}
