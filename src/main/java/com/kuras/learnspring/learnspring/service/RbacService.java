package com.kuras.learnspring.learnspring.service;

// com.kuras.learnspring.rbac.service.RbacService

import com.kuras.learnspring.learnspring.dto.AssignIdsRequest;
import com.kuras.learnspring.learnspring.dto.CreatePermissionRequest;
import com.kuras.learnspring.learnspring.dto.CreateProfileRequest;
import com.kuras.learnspring.learnspring.dto.CreateRoleRequest;
import com.kuras.learnspring.learnspring.entity.Permission;
import com.kuras.learnspring.learnspring.entity.Profile;
import com.kuras.learnspring.learnspring.entity.Role;
import com.kuras.learnspring.learnspring.entity.User;
import com.kuras.learnspring.learnspring.repository.PermissionRepository;
import com.kuras.learnspring.learnspring.repository.ProfileRepository;
import com.kuras.learnspring.learnspring.repository.RoleRepository;
import com.kuras.learnspring.learnspring.repository.UserRepository;
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
        return roleRepo.save(role);
    }

    @Transactional
    public Profile assignRolesToProfile(Long profileId, AssignIdsRequest req) {
        var profile = profileRepo.findById(profileId).orElseThrow();
        var roles = new HashSet<>(roleRepo.findAllById(req.ids()));
        profile.setRoles(roles);
        return profileRepo.save(profile);
    }

    @Transactional
    public User assignProfilesToUser(Long userId, AssignIdsRequest req) {
        var user = userRepo.findById(userId).orElseThrow();
        var profiles = new HashSet<>(profileRepo.findAllById(req.ids()));
        user.setProfiles(profiles);
        return userRepo.save(user);
    }
}
