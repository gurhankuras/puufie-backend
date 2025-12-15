package com.kuras.learnspring.learnspring.push_notification.repository;

import com.kuras.learnspring.learnspring.auth.entity.User;
import com.kuras.learnspring.learnspring.push_notification.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByDeviceToken(String deviceToken);
    Optional<Device> findByDeviceTokenAndUser(String deviceToken, User user);
    List<Device> findAllByUserIdAndIsActiveTrue(Long userId);

}
