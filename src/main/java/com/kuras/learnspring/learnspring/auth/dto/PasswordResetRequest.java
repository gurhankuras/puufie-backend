package com.kuras.learnspring.learnspring.auth.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {
    private String token;
    private String newPassword;
}
