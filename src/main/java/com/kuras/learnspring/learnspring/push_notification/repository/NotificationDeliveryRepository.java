package com.kuras.learnspring.learnspring.push_notification.repository;

import com.kuras.learnspring.learnspring.push_notification.entity.NotificationDelivery;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface NotificationDeliveryRepository extends JpaRepository<NotificationDelivery, Long> {
    @EntityGraph(attributePaths = {"device", "userNotification"})
    List<NotificationDelivery> findTop500ByStatusInAndAttemptCountLessThanOrderByCreatedAtAsc(
            Collection<NotificationDelivery.DeliveryStatus> statuses,
            int maxAttempts
    );
}
