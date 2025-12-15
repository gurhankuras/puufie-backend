package com.kuras.learnspring.learnspring.push_notification.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.SendResponse;
import com.kuras.learnspring.learnspring.push_notification.entity.NotificationDelivery;
import com.kuras.learnspring.learnspring.push_notification.mapper.FirebaseMapper;
import com.kuras.learnspring.learnspring.push_notification.repository.NotificationDeliveryRepository;
import com.kuras.learnspring.learnspring.push_notification.utility.FirebaseMessagingErrorUtility;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class NotificationWorker {

    private static final int MAX_ATTEMPTS = 5;

    private final NotificationDeliveryRepository deliveryRepository;
    private final FirebaseMessaging firebaseMessaging;

    public NotificationWorker(NotificationDeliveryRepository deliveryRepository, FirebaseApp firebaseApp) {
        this.deliveryRepository = deliveryRepository;
        this.firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
    }

    @Transactional
    @Scheduled(fixedDelay = 15000)
    public void processPendingDeliveries() {
        var retryableStatuses = List.of(
                NotificationDelivery.DeliveryStatus.PENDING,
                NotificationDelivery.DeliveryStatus.FAILED_TEMPORARY
        );

        var pendingDeliveries =
                deliveryRepository.findTop500ByStatusInAndAttemptCountLessThanOrderByCreatedAtAsc(
                        retryableStatuses,
                        MAX_ATTEMPTS
                );

        if (pendingDeliveries.isEmpty()) {
            return;
        }

        var messages = pendingDeliveries.stream()
                .map(FirebaseMapper::fromDeliveryToMessage)
                .toList();

        try {
            var batchResponse = firebaseMessaging.sendEach(messages);
            var responses = batchResponse.getResponses();

            int size = pendingDeliveries.size();

            for (int i = 0; i < size; i++) {
                var delivery = pendingDeliveries.get(i);
                var response = responses.get(i);

                if (response.isSuccessful()) {
                    handleSuccess(delivery, response);
                } else {
                    handleFailure(delivery, response);
                }
            }
            deliveryRepository.saveAll(pendingDeliveries);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send push notification batch", e);
        }
    }

    private void handleSuccess(NotificationDelivery delivery, SendResponse response) {
        delivery.setStatus(NotificationDelivery.DeliveryStatus.SENT);
        delivery.setProviderMessageId(response.getMessageId());
        delivery.setSentAt(Instant.now());
        delivery.setAttemptCount(delivery.getAttemptCount() + 1);
        delivery.setLastAttemptAt(Instant.now());
        delivery.setErrorCode(null);
        delivery.setErrorMessage(null);
    }

    private void handleFailure(NotificationDelivery delivery, SendResponse response) {
        var ex = response.getException();
        var errorCode = FirebaseMessagingErrorUtility.getErrorCode(ex);

        // DB’de sakla
        delivery.setErrorCode(errorCode != null ? errorCode.name() : null);
        delivery.setErrorMessage(ex.getMessage());
        delivery.setAttemptCount(delivery.getAttemptCount() + 1);
        delivery.setLastAttemptAt(Instant.now());

        // 1) Token tamamen ölmüş mü?
        if (errorCode != null && FirebaseMessagingErrorUtility.isDeadTokenError(errorCode)) {
            var device = delivery.getDevice();
            device.setActive(false); // Device entity'sinde active field'ı var
            delivery.setStatus(NotificationDelivery.DeliveryStatus.FAILED_PERMANENT);
            return;
        }

        // 2) Max attempt'i aştı mı?
        if (delivery.getAttemptCount() >= MAX_ATTEMPTS) {
            delivery.setStatus(NotificationDelivery.DeliveryStatus.FAILED_PERMANENT);
            return;
        }

        if (errorCode != null && FirebaseMessagingErrorUtility.isTemporaryError(errorCode)) {
            delivery.setStatus(NotificationDelivery.DeliveryStatus.FAILED_TEMPORARY);
        } else {
            delivery.setStatus(NotificationDelivery.DeliveryStatus.FAILED_PERMANENT);
        }
    }


}
