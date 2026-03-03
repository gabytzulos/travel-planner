package com.gabrielluciu.travelplanner.dto.trip;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record TripResponse(
        UUID id,
        UUID userId,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String notes,
        List<DestinationDto> destinations
) {
}
