package com.gabrielluciu.travelplanner.dto.auth;

public record LoginRequestDto(
        String email,
        String password
) {
}
