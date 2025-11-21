package com.kuras.learnspring.learnspring.security;

import com.kuras.learnspring.learnspring.access_control.entity.Permission;
import com.kuras.learnspring.learnspring.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsernameFetchJoin(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<String> perms = user.getProfiles().stream()
                .flatMap(p -> p.getRoles().stream())
                .flatMap(r -> r.getPermissions().stream())
                .map(Permission::getCode)
                .collect(Collectors.toSet());

        List<String> roleNames = user.getProfiles().stream()
                .flatMap(x -> x.getRoles().stream())
                .map(x -> x.getName())
                .toList();



        perms.addAll(roleNames);
        var authorities = perms.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new CustomUserDetails(user.getUsername(), user.getPassword(), authorities, roleNames);
    }
}
