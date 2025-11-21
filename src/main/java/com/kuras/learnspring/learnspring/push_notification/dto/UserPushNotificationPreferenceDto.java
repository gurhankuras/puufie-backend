package com.kuras.learnspring.learnspring.push_notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class UserPushNotificationPreferenceDto {
    private Long id;

    private boolean pushEnabled = true;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime quietHoursStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime quietHoursEnd;

    private String timezone;

    private Integer dailyCap;

}