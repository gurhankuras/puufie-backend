package com.kuras.learnspring.learnspring.push_notification.mapper;

import com.google.firebase.messaging.*;
import com.kuras.learnspring.learnspring.push_notification.entity.NotificationDelivery;

import java.util.Map;
import java.util.stream.Collectors;

public final class FirebaseMapper {
    public static Message fromDeliveryToMessage(NotificationDelivery delivery) {
        var notification = delivery.getUserNotification();
        var device = delivery.getDevice();
        var androidConfig = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .build();

        var apnsConfig = ApnsConfig.builder()
                .setAps(Aps.builder()
                    .setSound("default")
                    .setBadge(1)
                    .setMutableContent(true)
                    .build()
                )
                .putHeader("apns-push-type", "alert")
                .putHeader("apns-priority", "10")
                .build();

        var message = Message.builder()
                .setNotification(
                        Notification.builder()
                                .setBody(notification.getBody())
                                .setTitle(notification.getTitle())
                                .build()
                )
                .setAndroidConfig(androidConfig)
                .setApnsConfig(apnsConfig)
                .setToken(device.getDeviceToken())
                .putAllData(mapObjectMapToStringMap(notification.getData()))
                .build();

        return message;
    }

    private static Map<String, String> mapObjectMapToStringMap(Map<String, Object> source) {
        Map<String, String> result = source.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() == null ? null : e.getValue().toString()
                ));
        return result;
    }
}
