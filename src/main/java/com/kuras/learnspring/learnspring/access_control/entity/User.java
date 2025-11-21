package com.kuras.learnspring.learnspring.access_control.entity;

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

    @Column(nullable = false, length = 255)
    private String password;


    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isActive = true;

    @ManyToMany
    @JoinTable(name="user_profile",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="profile_id"))
    Set<Profile> profiles = new HashSet<>();
}
