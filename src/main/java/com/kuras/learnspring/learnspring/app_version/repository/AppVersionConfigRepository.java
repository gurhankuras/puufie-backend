package com.kuras.learnspring.learnspring.app_version.repository;

import com.kuras.learnspring.learnspring.app_version.entity.AppVersionConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppVersionConfigRepository extends JpaRepository<AppVersionConfig, Long> {
    Optional<AppVersionConfig> findByPlatform(String platform);
}
