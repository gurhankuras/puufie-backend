package com.kuras.learnspring.learnspring.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_security")
public class UserSecurity {
    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private int passwordPolicyVersionAtSet;

    @Column(nullable = false)
    private LocalDateTime passwordLastChangedAt;

    @Column(columnDefinition = "boolean default false")
    private boolean mustChangePassword;

    @Column(nullable = false)
    private int failedLoginAttempts;

    @Column(nullable = true)
    private LocalDateTime lockedUntil;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

}