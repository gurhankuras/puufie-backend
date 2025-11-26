package com.kuras.learnspring.learnspring.auth.repository;


import com.kuras.learnspring.learnspring.auth.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
}
