package com.gabrielluciu.travelplanner.dto.auth;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AuthResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String token
) {
}
