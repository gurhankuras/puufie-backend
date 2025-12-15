package com.kuras.learnspring.learnspring.push_notification.repository;

import com.kuras.learnspring.learnspring.push_notification.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    Page<UserNotification> findByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update UserNotification n
           set n.read = true,
               n.readAt = CURRENT_TIMESTAMP
         where n.user.id = :userId
           and n.read = false
    """)
    int markAllAsReadByUserId(@Param("userId") Long userId);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update UserNotification n
           set n.read = true,
               n.readAt = CURRENT_TIMESTAMP
         where n.id = :notificationId
           and n.user.id = :userId
           and n.read = false
    """)
    int markAsReadByIdAndUserId(@Param("notificationId") Long notificationId,
                                @Param("userId") Long userId);
}
