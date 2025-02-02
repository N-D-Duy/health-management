package com.example.health_management.common.shared.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    Permission.ALL_READ,
                    Permission.ALL_UPDATE,
                    Permission.ALL_CREATE,
                    Permission.ALL_DELETE
            )
    ),
    MANAGER(
            Set.of(
                    Permission.DOCTOR_READ,
                    Permission.DOCTOR_UPDATE,
                    Permission.DOCTOR_CREATE,
                    Permission.DOCTOR_DELETE
            )
    ),
    DOCTOR(Set.of(Permission.BASIC_READ)),
    USER(Set.of(Permission.BASIC_READ));
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}

