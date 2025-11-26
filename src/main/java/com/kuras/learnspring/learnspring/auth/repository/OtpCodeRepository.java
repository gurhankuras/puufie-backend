package com.kuras.learnspring.learnspring.auth.repository;

import com.kuras.learnspring.learnspring.auth.entity.OtpCode;
import com.kuras.learnspring.learnspring.auth.entity.OtpStatus;
import com.kuras.learnspring.learnspring.auth.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from OtpCode o where o.id = :id")
    Optional<OtpCode> findByIdForUpdate(@Param("id") Long id);

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
