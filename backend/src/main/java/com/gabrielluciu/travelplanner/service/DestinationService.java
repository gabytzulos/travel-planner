package com.gabrielluciu.travelplanner.service;

import com.gabrielluciu.travelplanner.dto.trip.DestinationDto;
import com.gabrielluciu.travelplanner.entity.Destination;
import com.gabrielluciu.travelplanner.repository.DestinationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    public Destination findOrCreateDestination(DestinationDto destDto) {
        return this.destinationRepository.findByCityAndCountry(
                destDto.city(), destDto.country()
        ).orElseGet(() -> destinationRepository.save(
                Destination.builder()
                        .city(destDto.city())
                        .country(destDto.country())
                        .build()
        ));
    }

}
