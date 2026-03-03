package com.gabrielluciu.travelplanner.dto.trip;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateTripRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    @NotEmpty(message = "At least one destination is required")
    @Size(max = 20, message = "Cannot have more than 20 destinations")
    @Valid
    private List<DestinationDto> destinations;

    private LocalDate startDate;
    private LocalDate endDate;

    private String notes;
}
