package com.gabrielluciu.travelplanner.dto.trip;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DestinationDto(

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City cannot exceed 100 characters")
        String city,

        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country cannot exceed 100 characters")
        String country

) {
    public DestinationDto {
        city = city == null ? null : city.trim().toLowerCase();
        country = country == null ? null : country.trim().toLowerCase();
    }
}
