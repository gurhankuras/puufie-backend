package com.kuras.learnspring.learnspring.auth.repository;

import com.kuras.learnspring.learnspring.auth.entity.OtpCode;
import com.kuras.learnspring.learnspring.auth.entity.OtpStatus;
import com.kuras.learnspring.learnspring.access_control.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    Optional<OtpCode> findTopByUserAndSubjectAndExpiresAtAfterAndStatusOrderByCreatedAtDesc(
            User user,
            String subject,
            LocalDateTime now,
            OtpStatus status
    );
    List<OtpCode> findAllByUserAndSubjectAndExpiresAtAfter(
            User user,
            String subject,
            LocalDateTime now
    );
}
