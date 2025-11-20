package com.kuras.learnspring.learnspring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuras.learnspring.learnspring.converters.ZoneIdAttributeConverter;
import com.kuras.learnspring.learnspring.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.time.ZoneId;

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