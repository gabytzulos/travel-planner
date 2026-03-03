package com.gabrielluciu.travelplanner.controller;


import com.gabrielluciu.travelplanner.dto.trip.CreateTripRequest;
import com.gabrielluciu.travelplanner.dto.trip.TripResponse;
import com.gabrielluciu.travelplanner.security.SecurityUtils;
import com.gabrielluciu.travelplanner.service.TripService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trip")
@AllArgsConstructor
public class TripController {

    private final TripService tripService;

    @GetMapping
    public List<TripResponse> getAllTrips() {
        return this.tripService.getAllUserTrips(SecurityUtils.getCurrentUserId());
    }

    @GetMapping("/{id}")
    public TripResponse getTripById(@PathVariable UUID id) {
        return this.tripService.getUserTripById(id);
    }

    @PostMapping
    public TripResponse createTrip(@Valid @RequestBody CreateTripRequest request) {
        return this.tripService.createTrip(SecurityUtils.getCurrentUserId(), request);
    }

}
