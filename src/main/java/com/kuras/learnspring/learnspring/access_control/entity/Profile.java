package com.kuras.learnspring.learnspring.access_control.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="profile")
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, length=100)
    private String name; // "FinanceProfile"
    private String description;

    @ManyToMany
    @JoinTable(name="profile_role",
            joinColumns=@JoinColumn(name="profile_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();
}
