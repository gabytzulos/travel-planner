package com.gabrielluciu.travelplanner.dto.auth;

import java.util.UUID;

public record LoginResponse(
        UUID id,
        String email,
        String firstName,
        String lastName
) {
    public LoginResponse(AuthResponse auth) {
        this(auth.id(), auth.email(), auth.firstName(), auth.lastName());
    }
}
