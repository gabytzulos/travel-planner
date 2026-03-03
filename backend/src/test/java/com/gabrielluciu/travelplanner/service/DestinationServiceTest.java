package com.gabrielluciu.travelplanner.service;

import com.gabrielluciu.travelplanner.dto.trip.DestinationDto;
import com.gabrielluciu.travelplanner.entity.Destination;
import com.gabrielluciu.travelplanner.repository.DestinationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DestinationServiceTest {

    @Mock
    private DestinationRepository destinationRepository;

    @InjectMocks
    private DestinationService destinationService;

    private static final String MOCK_CITY = "paris";
    private static final String MOCK_COUNTRY = "france";

    @Test
    void findOrCreateDestination_destinationExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        Destination mockDest = Destination.builder().id(id).city(MOCK_CITY).country(MOCK_COUNTRY).build();
        when(this.destinationRepository.findByCityAndCountry(MOCK_CITY, MOCK_COUNTRY)).thenReturn(Optional.of(mockDest));

        // Act
        Destination result = this.destinationService.findOrCreateDestination(new DestinationDto(MOCK_CITY, MOCK_COUNTRY));

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(MOCK_CITY, result.getCity());
        assertEquals(MOCK_COUNTRY, result.getCountry());

        verify(destinationRepository).findByCityAndCountry(MOCK_CITY, MOCK_COUNTRY);
    }

    @Test
    void findOrCreateDestination_destinationIsCreatedIfNotExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(this.destinationRepository.findByCityAndCountry(MOCK_CITY, MOCK_COUNTRY)).thenReturn(Optional.empty());

        when(this.destinationRepository.save(any(Destination.class))).thenAnswer(invocation -> {
            Destination d = invocation.getArgument(0);
            d.setId(id);
            return d;
        });

        // Act
        Destination result = this.destinationService.findOrCreateDestination(new DestinationDto(MOCK_CITY, MOCK_COUNTRY));

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(MOCK_CITY, result.getCity());
        assertEquals(MOCK_COUNTRY, result.getCountry());

        verify(destinationRepository).save(any(Destination.class));
    }
}