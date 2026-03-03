package com.gabrielluciu.travelplanner.repository;

import com.gabrielluciu.travelplanner.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, UUID> {
    Optional<Destination> findByCityAndCountry(String city, String country);
}
