package com.kuras.learnspring.learnspring.auth.repository;

import com.kuras.learnspring.learnspring.auth.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
      update PasswordResetToken t
         set t.used = true
       where t.tokenHash = :hash
         and t.used = false
         and t.expiresAt > CURRENT_TIMESTAMP
    """)
    int markTokenUsedIfValid(@Param("hash") String hash);

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);
}
