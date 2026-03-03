package com.gabrielluciu.travelplanner.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @Email
        @NotEmpty(message = "Email is required")
        String email,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password
) {
    public RegisterRequest {
        email = email == null ? null : email.trim();
        firstName = firstName == null ? null : firstName.trim();
        lastName = lastName == null ? null : lastName.trim();
    }
}
