package com.kuras.learnspring.learnspring.push_notification.entity;

import com.kuras.learnspring.learnspring.auth.entity.User;
import com.kuras.learnspring.learnspring.common.converters.ZoneIdAttributeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.time.ZoneId;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "user_push_preference")
public class UserPushPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "push_enabled", nullable = false)
    private boolean pushEnabled = true;

    @Column(name = "quiet_hours_start")
    private LocalTime quietHoursStart;

    @Column(name = "quiet_hours_end")
    private LocalTime quietHoursEnd;

    @Convert(converter = ZoneIdAttributeConverter.class)
    @Column(name = "timezone")
    private ZoneId timezone;

    @Column(name = "daily_cap", nullable = false)
    private Integer dailyCap = 20; // mirrors DEFAULT 20


    public static UserPushPreference getDefault() {
        return  UserPushPreference.builder()
                .pushEnabled(true)
                .quietHoursEnd(null)
                .quietHoursStart(null)
                .timezone(null)
                .build();
    }

}
