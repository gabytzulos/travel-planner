package com.gabrielluciu.travelplanner.security;

import java.util.Set;

public class JwtClaims {
    private JwtClaims() {
    }

    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String ROLES = "roles";

    public static final Set<String> RESERVED = Set.of("sub", ROLES, "exp", "iat", "iss", "jti");
}
