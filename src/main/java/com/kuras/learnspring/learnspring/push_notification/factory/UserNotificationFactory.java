package com.kuras.learnspring.learnspring.push_notification.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuras.learnspring.learnspring.auth.entity.User;
import com.kuras.learnspring.learnspring.push_notification.constant.NotificationType;
import com.kuras.learnspring.learnspring.push_notification.entity.UserNotification;
import com.kuras.learnspring.learnspring.push_notification.model.NotificationContext;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserNotificationFactory {

    private final EntityManager entityManager;


    public UserNotification create(NotificationContext ctx) {
        return switch (ctx.type()) {
            case PASSWORD_CHANGED -> buildPasswordChanged(ctx);
            case LOGIN_FROM_NEW_DEVICE -> buildLoginFromNewDevice(ctx);
            case GENERIC -> buildGeneric(ctx);
            case IMAGE_CONTENT -> buildImageContent(ctx);
        };
    }

    private UserNotification base(Long userId, NotificationType type) {
        var now = Instant.now();

        var userRef = entityManager.getReference(User.class, userId);

        return UserNotification.builder()
                .user(userRef)
                .notificationType(type.name())
                .read(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    private UserNotification buildPasswordChanged(NotificationContext ctx) {
        var n = base(ctx.userId(), NotificationType.PASSWORD_CHANGED);
        n.setCategory("SECURITY");
        n.setTitle("Şifren değişti");
        n.setBody("Eğer bu işlemi sen yapmadıysan güvenlik ayarlarını kontrol et.");
        n.setTarget("OPEN_SECURITY_SETTINGS");
        n.setData(Map.of(
                "screen", "security",
                "tab", "password",
                "ip", ctx.data().get("ip"),
                "device", ctx.data().get("device")
        ));
        return n;
    }


    private UserNotification buildLoginFromNewDevice(NotificationContext ctx) {
        var n = base(ctx.userId(), NotificationType.LOGIN_FROM_NEW_DEVICE);
        n.setCategory("SECURITY");
        n.setTitle("Yeni bir cihazdan giriş yapıldı");
        n.setBody("Eğer bu sen değilsen, hesabını hemen kontrol et.");
        n.setTarget("OPEN_SECURITY_SETTINGS");
        n.setData(Map.of(
                "ip", ctx.data().get("ip"),
                "device", ctx.data().get("device")
        ));
        return n;
    }

    private UserNotification buildGeneric(NotificationContext ctx) {
        var n = base(ctx.userId(), NotificationType.GENERIC);
        n.setCategory("SYSTEM");
        n.setTitle((String) ctx.data().getOrDefault("title", "Bildirim"));
        n.setBody((String) ctx.data().getOrDefault("body", ""));
        n.setTarget((String) ctx.data().getOrDefault("action", null));
        n.setData((Map<String, Object>) ctx.data().getOrDefault("data", Map.of()));
        return n;
    }

    private UserNotification buildImageContent(NotificationContext ctx) {
        var n = base(ctx.userId(), NotificationType.IMAGE_CONTENT);
        n.setCategory("SECURITY");
        n.setTitle("Fotolu bildirim");
        n.setBody("Ne kadar guzel bir fotograf");
        n.setTarget("OPEN_SECURITY_SETTINGS");
        n.setData(Map.of(
                "imageUrl", ctx.data().get("imageUrl")
        ));
        return n;
    }
}

