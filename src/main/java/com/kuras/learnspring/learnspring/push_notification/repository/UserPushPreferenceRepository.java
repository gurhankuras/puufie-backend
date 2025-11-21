package com.kuras.learnspring.learnspring.push_notification.repository;

import com.kuras.learnspring.learnspring.push_notification.entity.UserPushPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPushPreferenceRepository extends JpaRepository<UserPushPreference, Long> {
    Optional<UserPushPreference> getByUserId(long userId);
}
