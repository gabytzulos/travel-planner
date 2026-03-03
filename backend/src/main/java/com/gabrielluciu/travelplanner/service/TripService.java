package com.gabrielluciu.travelplanner.service;

import com.gabrielluciu.travelplanner.dto.trip.CreateTripRequest;
import com.gabrielluciu.travelplanner.dto.trip.DestinationDto;
import com.gabrielluciu.travelplanner.dto.trip.TripResponse;
import com.gabrielluciu.travelplanner.entity.Trip;
import com.gabrielluciu.travelplanner.entity.auth.User;
import com.gabrielluciu.travelplanner.exception.ResourceNotFoundException;
import com.gabrielluciu.travelplanner.repository.TripRepository;
import com.gabrielluciu.travelplanner.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final DestinationService destinationService;

    public TripResponse createTrip(UUID userId, CreateTripRequest request) {
        Trip trip = Trip.builder()
                .user(new User(userId)) // no need to fetch the user
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .notes(request.getNotes())
                .destinations(new ArrayList<>())
                .build();

        this.appendDestinationsToTrip(trip, request.getDestinations());
        this.tripRepository.save(trip);

        return tripEntityToDto(trip);
    }

    public List<TripResponse> getAllUserTrips(UUID userId) {
        List<Trip> trips = this.tripRepository.findByUser(new User(userId));

        return trips.stream().map(this::tripEntityToDto).toList();
    }

    public TripResponse getUserTripById(UUID tripId) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        Trip trip = this.tripRepository.findByIdAndUserId(tripId, currentUserId).orElseThrow(() -> new ResourceNotFoundException("Trip not found."));

        return this.tripEntityToDto(trip);
    }

    private TripResponse tripEntityToDto(Trip trip) {
        List<DestinationDto> destinationDtos = trip.getDestinations().stream()
                .map(d -> DestinationDto.builder()
                        .city(d.getCity())
                        .country(d.getCountry())
                        .build()
                ).toList();

        return TripResponse.builder()
                .id(trip.getId())
                .userId(trip.getUser().getId())
                .title(trip.getTitle())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .notes(trip.getNotes())
                .destinations(destinationDtos)
                .build();
    }

    private void appendDestinationsToTrip(Trip trip, List<DestinationDto> destDtos) {
        for (var destDto : destDtos) {
            trip.getDestinations().add(this.destinationService.findOrCreateDestination(destDto));
        }
    }

}
