package com.kuras.learnspring.learnspring.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuras.learnspring.learnspring.auth.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String countryCode;
    private String phoneNumber;

    @JsonProperty("fullPhoneNumber")
    public String getFullPhoneNumber() {
        return countryCode + phoneNumber;
    }

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .countryCode(user.getCountryCode())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
