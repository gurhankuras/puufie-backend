package com.kuras.learnspring.learnspring.localization.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "localization_entry",
        indexes = {
                @Index(
                        name = "idx_localization_entry_locale_version",
                        columnList = "locale, version"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_localization_entry_locale_key_version",
                        columnNames = {"locale", "key", "version"}
                )
        }
)
public class LocalizationEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String locale;

    private String key;

    @Column(name = "value")
    private String value;

    private Integer version;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

