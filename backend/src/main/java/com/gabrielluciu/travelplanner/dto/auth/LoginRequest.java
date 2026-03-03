package com.gabrielluciu.travelplanner.dto.auth;

public record LoginRequest(
        String email,
        String password
) {
}
