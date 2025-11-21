package com.kuras.learnspring.learnspring.common.mapper;

import com.kuras.learnspring.learnspring.push_notification.dto.UserPushNotificationPreferenceDto;
import com.kuras.learnspring.learnspring.push_notification.entity.UserPushPreference;

import java.time.ZoneId;

public final class UserPushPreferenceMapper {

    public static UserPushNotificationPreferenceDto toDto(UserPushPreference e) {
        if (e == null) return null;
        return UserPushNotificationPreferenceDto.builder()
                .id(e.getId())
                .pushEnabled(e.isPushEnabled())
                .quietHoursStart(e.getQuietHoursStart())
                .quietHoursEnd(e.getQuietHoursEnd())
                .timezone(e.getTimezone() != null ? e.getTimezone().getId() : null)
                .dailyCap(e.getDailyCap())
                .build();
    }

    public static UserPushPreference toEntity(UserPushNotificationPreferenceDto dto) {
        if (dto == null) return null;
        return UserPushPreference.builder()
                .id(dto.getId())
                .pushEnabled(dto.isPushEnabled())
                .quietHoursStart(dto.getQuietHoursStart())
                .quietHoursEnd(dto.getQuietHoursEnd())
                .timezone(dto.getTimezone() != null ? stringToZoneId(dto.getTimezone()) : null)
                .dailyCap(dto.getDailyCap())
                .build();
    }

    public static void updateEntity(UserPushNotificationPreferenceDto dto, UserPushPreference entity) {
        entity.setPushEnabled(dto.isPushEnabled());
        entity.setQuietHoursStart(dto.getQuietHoursStart());
        entity.setQuietHoursEnd(dto.getQuietHoursEnd());
        entity.setTimezone(dto.getTimezone() != null ? stringToZoneId(dto.getTimezone()) : null);
        entity.setDailyCap(dto.getDailyCap());
    }

    // ----- helpers -----
    private static String zoneIdToString(ZoneId z) {
        return z != null ? z.getId() : null;
    }

    private static ZoneId stringToZoneId(String s) {
        return s != null && !s.isBlank() ? ZoneId.of(s) : null;
    }
}