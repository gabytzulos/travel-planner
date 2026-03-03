package com.gabrielluciu.travelplanner.security;

import com.gabrielluciu.travelplanner.entity.auth.User;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class CustomUserDetails implements UserDetails {

    private final String passwordHash;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    @Getter
    private final String email;

    @Getter
    private final UUID id;

    @Getter
    private final String firstName;

    @Getter
    private final String lastName;

    @Getter
    private final boolean emailVerified;

    public CustomUserDetails(
            User user,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.passwordHash = user.getPasswordHash();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.emailVerified = user.isEmailVerified();
        this.enabled = user.isEnabled();
        this.authorities = List.copyOf(authorities);
    }

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return this.passwordHash;
    }

    @Override
    public @NonNull String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
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

}
