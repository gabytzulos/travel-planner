package com.gabrielluciu.travelplanner.service;

import com.gabrielluciu.travelplanner.dto.trip.CreateTripRequest;
import com.gabrielluciu.travelplanner.dto.trip.DestinationDto;
import com.gabrielluciu.travelplanner.dto.trip.TripResponse;
import com.gabrielluciu.travelplanner.entity.Trip;
import com.gabrielluciu.travelplanner.entity.auth.User;
import com.gabrielluciu.travelplanner.exception.ResourceNotFoundException;
import com.gabrielluciu.travelplanner.repository.TripRepository;
import com.gabrielluciu.travelplanner.security.CurrentUser;
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
    private final CurrentUser currentUser;

    public TripResponse createTrip(CreateTripRequest request) {
        Trip trip = Trip.builder()
                .user(new User(currentUser.getId())) // no need to fetch the user
                .title(request.title())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .notes(request.notes())
                .destinations(new ArrayList<>())
                .build();

        this.appendDestinationsToTrip(trip, request.destinations());
        this.tripRepository.save(trip);

        return tripEntityToDto(trip);
    }

    public List<TripResponse> getAllUserTrips() {
        List<Trip> trips = this.tripRepository.findByUser(new User(currentUser.getId()));
        return trips.stream().map(this::tripEntityToDto).toList();
    }

    public TripResponse getUserTripById(UUID tripId) {
        Trip trip = this.tripRepository.findByIdAndUserId(tripId, currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Trip not found."));
        return this.tripEntityToDto(trip);
    }

    private TripResponse tripEntityToDto(Trip trip) {
        List<DestinationDto> destinationDtos = trip.getDestinations().stream()
                .map(d -> new DestinationDto(d.getCity(), d.getCountry())).toList();

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
        // todo: improve by using a hash for each location and make a batched query
        for (var destDto : destDtos) {
            trip.getDestinations().add(this.destinationService.findOrCreateDestination(destDto));
        }
    }

}
