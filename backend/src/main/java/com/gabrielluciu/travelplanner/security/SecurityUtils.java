package com.gabrielluciu.travelplanner.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public class SecurityUtils {
    private SecurityUtils() {
    }

    public static UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        Jwt jwt = (Jwt) auth.getPrincipal();
        if (jwt == null) {
            throw new IllegalStateException("Invalid Jwt");
        }

        return UUID.fromString(jwt.getSubject());
    }

}
