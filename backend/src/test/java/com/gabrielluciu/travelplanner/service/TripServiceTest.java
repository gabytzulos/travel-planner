package com.gabrielluciu.travelplanner.service;

import com.gabrielluciu.travelplanner.dto.trip.CreateTripRequest;
import com.gabrielluciu.travelplanner.dto.trip.DestinationDto;
import com.gabrielluciu.travelplanner.dto.trip.TripResponse;
import com.gabrielluciu.travelplanner.entity.Destination;
import com.gabrielluciu.travelplanner.entity.Trip;
import com.gabrielluciu.travelplanner.entity.auth.User;
import com.gabrielluciu.travelplanner.exception.ResourceNotFoundException;
import com.gabrielluciu.travelplanner.repository.TripRepository;
import com.gabrielluciu.travelplanner.security.CurrentUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private DestinationService destinationService;

    @Mock
    private CurrentUser currentUser;

    @InjectMocks
    private TripService tripService;

    public static final String MOCK_TRIP_TITLE = "Paris trip 2026";
    public static final String MOCK_TRIP_NOTES = "First trip to Paris!";
    public static final String MOCK_CITY = "paris";
    public static final String MOCK_COUNTRY = "paris";

    @Test
    void createTrip_success() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID tripId = UUID.randomUUID();
        when(currentUser.getId()).thenReturn(userId);

        List<DestinationDto> destDtos = List.of(new DestinationDto(MOCK_CITY, MOCK_COUNTRY));
        CreateTripRequest request = new CreateTripRequest(
                MOCK_TRIP_TITLE,
                destDtos,
                LocalDate.of(2026, 3, 10),
                LocalDate.of(2026, 3, 20),
                MOCK_TRIP_NOTES
        );

        Destination mockDestination = Destination.builder().city(MOCK_CITY).country(MOCK_COUNTRY).build();
        when(destinationService.findOrCreateDestination(any())).thenReturn(mockDestination);
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> {
            Trip t = invocation.getArgument(0);
            t.setId(tripId);
            return t;
        });

        // Act
        TripResponse response = this.tripService.createTrip(request);

        // Assert
        assertEquals(userId, response.userId());
        assertEquals(tripId, response.id());
        assertEquals(MOCK_TRIP_TITLE, response.title());
        assertEquals(MOCK_TRIP_NOTES, response.notes());
        assertEquals(1, response.destinations().size());
        assertEquals(MOCK_CITY, response.destinations().getFirst().city());
        assertEquals(MOCK_COUNTRY, response.destinations().getFirst().country());

        verify(tripRepository, times(1)).save(any(Trip.class));
        verify(currentUser).getId();
    }

    @Test
    void getAllUserTrips() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID trip1Id = UUID.randomUUID();
        UUID trip2Id = UUID.randomUUID();
        when(currentUser.getId()).thenReturn(userId);

        Trip trip1 = this.getMockTrip(trip1Id, userId);
        Trip trip2 = this.getMockTrip(trip2Id, userId);

        when(tripRepository.findByUser(any(User.class))).thenReturn(List.of(trip1, trip2));

        // Act
        List<TripResponse> response = this.tripService.getAllUserTrips();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());

        verify(tripRepository).findByUser(any(User.class));
    }

    @Test
    void getUserTripById_success() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID tripId = UUID.randomUUID();
        when(currentUser.getId()).thenReturn(userId);

        Trip trip = this.getMockTrip(tripId, userId);
        when(tripRepository.findByIdAndUserId(tripId, userId)).thenReturn(Optional.of(trip));

        // Act
        TripResponse response = this.tripService.getUserTripById(tripId);

        // Assert
        assertNotNull(response);
        assertEquals(userId, response.userId());
        assertEquals(tripId, response.id());
        assertEquals(MOCK_TRIP_TITLE, response.title());
        assertEquals(MOCK_TRIP_NOTES, response.notes());
        assertEquals(1, response.destinations().size());
        assertEquals(MOCK_CITY, response.destinations().getFirst().city());
        assertEquals(MOCK_COUNTRY, response.destinations().getFirst().country());

        verify(tripRepository).findByIdAndUserId(tripId, userId);
        verify(currentUser).getId();
    }

    @Test
    void getUserTripById_shouldThrowResourceNotFound_whenTripDoesNotExist() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID tripId = UUID.randomUUID();

        when(currentUser.getId()).thenReturn(userId);
        when(tripRepository.findByIdAndUserId(tripId, userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> tripService.getUserTripById(tripId));
    }

    private Trip getMockTrip(UUID tripId, UUID userId) {
        Destination mockDestination = Destination.builder().city(MOCK_CITY).country(MOCK_COUNTRY).build();

        return Trip.builder()
                .id(tripId)
                .user(new User(userId))
                .title(MOCK_TRIP_TITLE)
                .notes(MOCK_TRIP_NOTES)
                .destinations(List.of(mockDestination))
                .startDate(LocalDate.of(2026, 3, 10))
                .endDate(LocalDate.of(2026, 3, 20))
                .build();
    }

}
