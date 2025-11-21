package com.kuras.learnspring.learnspring.access_control.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProfileRequest(
        @NotBlank String name,
        String description
) { }
