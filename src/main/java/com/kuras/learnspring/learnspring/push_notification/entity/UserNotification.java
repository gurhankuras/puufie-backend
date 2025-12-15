package com.kuras.learnspring.learnspring.push_notification.entity;

import com.kuras.learnspring.learnspring.auth.entity.User;
import com.kuras.learnspring.learnspring.common.converters.JsonMapConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_notification")
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "notification_type", length = 50, nullable = false)
    private String notificationType;

    @Column(name = "target", length = 50)
    private String target;

    @Convert(converter = JsonMapConverter.class)
    @Column(name = "data", columnDefinition = "JSON")
    private Map<String, Object> data;

    // Kullanıcı durumu
    @Column(name = "is_read", nullable = false)
    private boolean read;

    @Column(name = "read_at")
    private Instant readAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    // Zamanlar
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        var now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
