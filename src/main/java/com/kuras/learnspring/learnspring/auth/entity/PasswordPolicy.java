package com.kuras.learnspring.learnspring.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "password_policy")
public class PasswordPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private int version;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int minLength;

    @Column(nullable = false)
    private int maxLength;

    @Column(nullable = false)
    private boolean requireUpperCase;

    @Column(nullable = false)
    private boolean requireLowerCase;

    @Column(nullable = false)
    private boolean requireNumber;

    @Column(nullable = false)
    private boolean requireSpecial;

    @Column(nullable = false)
    private LocalDate validFrom;

    @Column(nullable = false)
    private LocalDate validTo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private Integer passwordExpiryDays;

    @Column(nullable = true)
    private Integer passwordHistoryLimit;

    @Column(nullable = true)
    private Integer maxFailedAttempts;

    @Column(nullable = true)
    private Integer lockoutDurationMinutes;
}
