package com.gabrielluciu.travelplanner.dto.auth;

import java.util.UUID;

public record LoginResponseDto(
        UUID id,
        String email,
        String firstName,
        String lastName
) {
    public LoginResponseDto(AuthResponseDto auth) {
        this(auth.id(), auth.email(), auth.firstName(), auth.lastName());
    }
}
