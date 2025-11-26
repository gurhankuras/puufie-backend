package com.kuras.learnspring.learnspring.access_control.service;

// com.kuras.learnspring.rbac.service.RbacService

import com.kuras.learnspring.learnspring.access_control.dto.AssignIdsRequest;
import com.kuras.learnspring.learnspring.access_control.dto.CreatePermissionRequest;
import com.kuras.learnspring.learnspring.access_control.dto.CreateProfileRequest;
import com.kuras.learnspring.learnspring.access_control.dto.CreateRoleRequest;
import com.kuras.learnspring.learnspring.access_control.entity.Permission;
import com.kuras.learnspring.learnspring.access_control.entity.Profile;
import com.kuras.learnspring.learnspring.access_control.entity.Role;
import com.kuras.learnspring.learnspring.auth.entity.User;
import com.kuras.learnspring.learnspring.access_control.repository.PermissionRepository;
import com.kuras.learnspring.learnspring.access_control.repository.ProfileRepository;
import com.kuras.learnspring.learnspring.access_control.repository.RoleRepository;
import com.kuras.learnspring.learnspring.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class RbacService {

    private final PermissionRepository permissionRepo;
    private final RoleRepository roleRepo;
    private final ProfileRepository profileRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder; // BCrypt vs.

    // ---- create ----
    public Permission createPermission(CreatePermissionRequest req) {
        var p = new Permission();
        p.setCode(req.code());
        p.setDescription(req.description());
        return permissionRepo.save(p);
    }

    public Role createRole(CreateRoleRequest req) {
        var r = new Role();
        r.setName(req.name());
        r.setDescription(req.description());
        return roleRepo.save(r);
    }

    public Profile createProfile(CreateProfileRequest req) {
        var p = new Profile();
        p.setName(req.name());
        p.setDescription(req.description());
        return profileRepo.save(p);
    }

    // ---- assign ----
    @Transactional
    public Role assignPermissionsToRole(Long roleId, AssignIdsRequest req) {
        var role = roleRepo.findById(roleId).orElseThrow();
        var perms = new HashSet<>(permissionRepo.findAllById(req.ids()));
        role.setPermissions(perms);
        return role;
    }

    @Transactional
    public Profile assignRolesToProfile(Long profileId, AssignIdsRequest req) {
        var profile = profileRepo.findById(profileId).orElseThrow();
        var roles = new HashSet<>(roleRepo.findAllById(req.ids()));
        profile.setRoles(roles);
        return profile;
    }

    @Transactional
    public User assignProfilesToUser(Long userId, AssignIdsRequest req) {
        var user = userRepo.findById(userId).orElseThrow();
        var profiles = new HashSet<>(profileRepo.findAllById(req.ids()));
        user.setProfiles(profiles);
        return user;
    }
}
