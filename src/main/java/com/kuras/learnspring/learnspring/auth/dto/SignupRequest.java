package com.kuras.learnspring.learnspring.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "^\\+[0-9]{1,3}$") String countryCode,
        @NotBlank @Pattern(regexp = "^[0-9]{5,15}$") String phoneNumber
) {}