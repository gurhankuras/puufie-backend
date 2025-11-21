package com.kuras.learnspring.learnspring.app_version.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_version_config")
public class AppVersionConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    private String platform;

    @Column(nullable = false, length = 10)
    private String minVersion;

    @Column(nullable = false, length = 10)
    private String latestVersion;

    @Column(nullable = false)
    private String storeUrl;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
