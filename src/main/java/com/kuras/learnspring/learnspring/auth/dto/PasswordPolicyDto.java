package com.kuras.learnspring.learnspring.auth.dto;

import com.kuras.learnspring.learnspring.auth.entity.PasswordPolicy;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordPolicyDto extends PasswordRequirementsDto {
    private Long id;
    private int version;
    private String name;

    private LocalDateTime createdAt;

    private Integer passwordExpiryDays;
    private Integer passwordHistoryLimit;
    private Integer maxFailedAttempts;
    private Integer lockoutDurationMinutes;

        public static PasswordPolicyDto from(PasswordPolicy policy) {
            var dto = new PasswordPolicyDto();
            dto.setId(policy.getId());
            dto.setName(policy.getName());
            dto.setVersion(policy.getVersion());
            dto.setPasswordExpiryDays(policy.getPasswordExpiryDays());
            dto.setPasswordHistoryLimit(policy.getPasswordHistoryLimit());
            dto.setMinLength(policy.getMinLength());
            dto.setMaxLength(policy.getMaxLength());
            dto.setRequireNumber(policy.isRequireNumber());
            dto.setRequireSpecial(policy.isRequireSpecial());
            dto.setRequireLowerCase(policy.isRequireLowerCase());
            dto.setRequireUpperCase(policy.isRequireUpperCase());
            dto.setLockoutDurationMinutes(policy.getLockoutDurationMinutes());
            dto.setMaxFailedAttempts(policy.getMaxFailedAttempts());
            return dto;
        }
}
