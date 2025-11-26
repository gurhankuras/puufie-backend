package com.kuras.learnspring.learnspring.auth.entity;

import com.kuras.learnspring.learnspring.access_control.entity.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "app_user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 150)
    private String username;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isActive = true;

    // First / Last name
    // @NotBlank
    @Column(name = "first_name", nullable = true, length = 100)
    private String firstName;

    // @NotBlank
    @Column(name = "last_name", nullable = true, length = 100)
    private String lastName;

    // Email
    // @NotBlank
    // @Email
    @Column(name = "email", nullable = true, length = 254)
    private String email;

    // Phone (country code + national number)
    // countryCode: "+90", "+1" ... max 4 char yeterli
    //@NotBlank
    // @Pattern(regexp = "^\\+[0-9]{1,3}$")
    @Column(name = "country_code", nullable = true, length = 4)
    private String countryCode;

    // phoneNumber: sadece rakam, 5-15 arası genel pratik
    // @Pattern(regexp = "^[0-9]{5,15}$")
    @Column(name = "phone_number", nullable = true, length = 15)
    private String phoneNumber;

    // (Opsiyonel) Tam E.164 gösterim; DB'ye yazmak istemezseniz @Transient bırakın
    @Transient
    public String getPhoneE164() {
        return countryCode + phoneNumber;
    }

    @ManyToMany
    @JoinTable(name="user_profile",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="profile_id"))
    Set<Profile> profiles = new HashSet<>();
}
