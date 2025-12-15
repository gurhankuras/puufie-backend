package com.kuras.learnspring.learnspring.push_notification.service;

import com.google.firebase.messaging.*;
import com.kuras.learnspring.learnspring.push_notification.entity.NotificationDelivery;
import com.kuras.learnspring.learnspring.push_notification.factory.UserNotificationFactory;
import com.kuras.learnspring.learnspring.push_notification.model.NotificationContext;
import com.kuras.learnspring.learnspring.push_notification.repository.DeviceRepository;
import com.kuras.learnspring.learnspring.push_notification.repository.NotificationDeliveryRepository;
import com.kuras.learnspring.learnspring.push_notification.repository.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationCommandService {

    private final UserNotificationFactory notificationFactory;
    private final UserNotificationRepository userNotificationRepository;
    private final DeviceRepository deviceRepository;
    private final NotificationDeliveryRepository deliveryRepository;

    @Transactional
    public Long createAndQueue(NotificationContext ctx) {

        // 1) Factory'den notification üret
        var notification = notificationFactory.create(ctx);

        // 2) DB'ye kaydet
        final var savedNotification = userNotificationRepository.save(notification);

        // 3) Kullanıcının tüm cihazlarını al
        var devices = deviceRepository.findAllByUserIdAndIsActiveTrue(ctx.userId());

        // 4) Her cihaz için PENDING delivery oluştur
        var deliveries = devices.stream()
                .map(device -> NotificationDelivery.builder()
                        .userNotification(savedNotification)
                        .device(device)
                        .status(NotificationDelivery.DeliveryStatus.PENDING)
                        .attemptCount(0)
                        .build()
                )
                .toList();

        deliveryRepository.saveAll(deliveries);

        // Kullanıcıya ID dön
        return notification.getId();
    }
}

