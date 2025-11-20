package com.kuras.learnspring.learnspring.controllers;

import com.kuras.learnspring.learnspring.dto.AssignIdsRequest;
import com.kuras.learnspring.learnspring.dto.CreatePermissionRequest;
import com.kuras.learnspring.learnspring.dto.CreateProfileRequest;
import com.kuras.learnspring.learnspring.dto.CreateRoleRequest;
import com.kuras.learnspring.learnspring.entity.Permission;
import com.kuras.learnspring.learnspring.entity.Profile;
import com.kuras.learnspring.learnspring.entity.Role;
import com.kuras.learnspring.learnspring.entity.User;
import com.kuras.learnspring.learnspring.repository.UserRepository;
import com.kuras.learnspring.learnspring.service.RbacService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/access-control")
@RequiredArgsConstructor
public class RbacAdminController {

    private final RbacService rbac;
    private final UserRepository userRepo;

    @PreAuthorize("hasAuthority('rbac.write')")
    @PostMapping("/permissions")
    public Permission createPermission(@RequestBody @Valid CreatePermissionRequest req) {
        return rbac.createPermission(req);
    }

    @PreAuthorize("hasAuthority('rbac.write')")
    @PostMapping("/roles")
    public Role createRole(@RequestBody @Valid CreateRoleRequest req) {
        return rbac.createRole(req);
    }

    @PreAuthorize("hasAuthority('rbac.write')")
    @PostMapping("/profiles")
    public Profile createProfile(@RequestBody @Valid CreateProfileRequest req) {
        return rbac.createProfile(req);
    }

    // ---- assign ----
    @PreAuthorize("hasAuthority('rbac.write')")
    @PostMapping("/roles/{roleId}/permissions")
    public Role assignPermissionsToRole(@PathVariable Long roleId,
                                        @RequestBody @Valid AssignIdsRequest req) {
        return rbac.assignPermissionsToRole(roleId, req);
    }

    @PreAuthorize("hasAuthority('rbac.write')")
    @PostMapping("/profiles/{profileId}/roles")
    public Profile assignRolesToProfile(@PathVariable Long profileId,
                                        @RequestBody @Valid AssignIdsRequest req) {
        return rbac.assignRolesToProfile(profileId, req);
    }

    @PreAuthorize("hasAuthority('rbac.write')")
    @PostMapping("/users/{userId}/profiles")
    public User assignProfilesToUser(@PathVariable Long userId,
                                     @RequestBody @Valid AssignIdsRequest req) {
        return rbac.assignProfilesToUser(userId, req);
    }

    // ---- read effective authorities (permissions + roles) ----
    @PreAuthorize("hasAuthority('rbac.read')")
    @GetMapping("/users/{userId}/authorities")
    public Set<String> getUserAuthorities(@PathVariable Long userId) {
        var u = userRepo.findById(userId).orElseThrow();
        var perms = u.getProfiles().stream()
                .flatMap(p -> p.getRoles().stream())
                .flatMap(r -> r.getPermissions().stream())
                .map(Permission::getCode)
                .collect(Collectors.toSet());

        // İstersen role adlarını da dahil et
        u.getProfiles().forEach(p -> p.getRoles().forEach(r -> perms.add(r.getName())));
        return perms;
    }
}
