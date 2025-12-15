package com.kuras.learnspring.learnspring.push_notification.service;


import com.kuras.learnspring.learnspring.auth.entity.User;
import com.kuras.learnspring.learnspring.auth.repository.UserRepository;

import com.kuras.learnspring.learnspring.push_notification.dto.RegisterPushTokenRequest;
import com.kuras.learnspring.learnspring.push_notification.entity.Device;
import com.kuras.learnspring.learnspring.push_notification.repository.DeviceRepository;
import com.kuras.learnspring.learnspring.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PushTokenService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Transactional
    public void registerToken(RegisterPushTokenRequest request, CustomUserDetails user) {
        if (user == null) {
            registerAnonymousToken(request);
        } else {
            registerTokenForUser(request, user.getUsername());
        }
    }

    private void registerAnonymousToken(RegisterPushTokenRequest request) {
        var now = LocalDateTime.now();
        var token = request.getToken();

        // Bu token daha önce kayıtlıysa onu güncelle
        var device = deviceRepository.findByDeviceToken(token)
                .orElseGet(() -> Device.builder()
                        .deviceToken(token)
                        .platform(request.getPlatform())
                        .build()
                );

        device.setOsVersion(request.getOsVersion());
        device.setAppVersion(request.getAppVersion());
        device.setLastSeenAt(now);

        // user = null kalsın (anonim)
        device.setUser(null);
        deviceRepository.save(device);
    }


    private void registerTokenForUser(RegisterPushTokenRequest request, String username) {
        var now = LocalDateTime.now();
        var token = request.getToken();

        // Gerçek user'ı backend buluyor, client ID gönderemiyor
        var user = userRepository.findByUsernameOrThrow(username);

        Device device;

        // 2.a) Aynı user + aynı token zaten varsa → onu kullan
        var existingForUserAndToken = deviceRepository.findByDeviceTokenAndUser(token, user);

        if (existingForUserAndToken.isPresent()) {
            device = existingForUserAndToken.get();
        } else {
            // 2.b) Token daha önce anonim olarak kayıt edilmiş olabilir
            var existingByToken = deviceRepository.findByDeviceToken(token).orElse(null);

            if (existingByToken != null && existingByToken.getUser() == null) {
                // anonim kaydı bu user'a bağla
                device = existingByToken;
                device.setUser(user);
                // dual mapping kullanıyorsan:
                // device.setUserId(user.getId());
            } else {
                // 2.c) Bu token daha önce hiç yok → yeni Device
                device = Device.builder()
                        .user(user)
                        // dual mapping varsa:
                        // .userId(user.getId())
                        .deviceToken(token)
                        .platform(request.getPlatform())
                        .build();
            }
        }

        device.setOsVersion(request.getOsVersion());
        device.setAppVersion(request.getAppVersion());
        device.setLastSeenAt(now);

        deviceRepository.save(device);
    }
}
