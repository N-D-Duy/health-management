package com.example.health_management.application.guards;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyUserDetails implements UserDetails {
    private final Collection<? extends GrantedAuthority> authorities;
    private final Long id;
    private final Integer version;
    private final String email;

    public MyUserDetails(Long id, String email, Collection<? extends GrantedAuthority> authorities, Integer version) {
        this.id = id;
        this.email = email;
        this.authorities = authorities;
        this.version = version;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
