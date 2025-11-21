package com.kuras.learnspring.learnspring.access_control.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePermissionRequest(
        @NotBlank String code,
        String description
) { }
