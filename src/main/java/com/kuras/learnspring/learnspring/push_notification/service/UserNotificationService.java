package com.kuras.learnspring.learnspring.push_notification.service;

import com.kuras.learnspring.learnspring.push_notification.entity.UserNotification;
import com.kuras.learnspring.learnspring.push_notification.repository.UserNotificationRepository;
import com.kuras.learnspring.learnspring.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserNotificationService {
    private final UserNotificationRepository userNotificationRepository;

    @Transactional(readOnly = true)
    public Page<UserNotification> getUserNotifications(Long userId, int page, int size) {
        var sort = Sort.by(Sort.Direction.DESC, "createdAt");
        var pageable = PageRequest.of(page, size, sort);
        return userNotificationRepository
                .findByUserIdAndDeletedAtIsNull(userId, pageable);
    }

    @Transactional
    public void markAllRead(Long userId) {
        userNotificationRepository.markAllAsReadByUserId(userId);
    }

    @Transactional
    public void markRead(Long notificationId, Long userId) {
        int updated = userNotificationRepository.markAsReadByIdAndUserId(notificationId, userId);
    }
}
