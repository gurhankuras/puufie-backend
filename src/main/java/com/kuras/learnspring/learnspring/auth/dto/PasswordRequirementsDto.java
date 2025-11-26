package com.kuras.learnspring.learnspring.auth.dto;

import com.kuras.learnspring.learnspring.auth.entity.PasswordPolicy;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequirementsDto {
    private int minLength;

    private int maxLength;

    private boolean requireUpperCase;

    private boolean requireLowerCase;

    private boolean requireNumber;

    private boolean requireSpecial;

    public static PasswordRequirementsDto from(PasswordPolicy policy) {
        return PasswordRequirementsDto.builder()
                .minLength(policy.getMinLength())
                .maxLength(policy.getMaxLength())
                .requireNumber(policy.isRequireNumber())
                .requireSpecial(policy.isRequireSpecial())
                .requireLowerCase(policy.isRequireLowerCase())
                .requireUpperCase(policy.isRequireUpperCase())
                .build();
    }

}
