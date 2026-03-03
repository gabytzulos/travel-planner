package com.gabrielluciu.travelplanner.dto.trip;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CreateTripRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title cannot exceed 200 characters")
        String title,

        @NotEmpty(message = "At least one destination is required")
        @Size(max = 20, message = "Cannot have more than 20 destinations")
        @Valid
        List<DestinationDto> destinations,

        LocalDate startDate,
        LocalDate endDate,
        String notes
) {
}
