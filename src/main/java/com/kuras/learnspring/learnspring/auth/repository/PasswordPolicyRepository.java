package com.kuras.learnspring.learnspring.auth.repository;

import com.kuras.learnspring.learnspring.auth.entity.PasswordPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordPolicyRepository extends JpaRepository<PasswordPolicy, Long> {
    @Query(value = """
        SELECT *
        FROM password_policy
        WHERE valid_from <= NOW()
          AND (valid_to IS NULL OR valid_to >= NOW())
        ORDER BY version DESC
        LIMIT 1
        """, nativeQuery = true)
    Optional<PasswordPolicy> findCurrentPolicy();
}
