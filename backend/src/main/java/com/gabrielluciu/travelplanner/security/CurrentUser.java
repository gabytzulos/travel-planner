package com.gabrielluciu.travelplanner.security;

import com.gabrielluciu.travelplanner.exception.NoAuthenticatedUserException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUser {

    public UUID getId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new NoAuthenticatedUserException();
        }

        Jwt jwt = (Jwt) auth.getPrincipal();
        if (jwt == null) {
            throw new IllegalStateException("Invalid Jwt");
        }

        return UUID.fromString(jwt.getSubject());
    }

}
