package com.kuras.learnspring.learnspring.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProfileRequest(
        @NotBlank String name,
        String description
) { }
