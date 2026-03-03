package com.gabrielluciu.travelplanner.repository;


import com.gabrielluciu.travelplanner.entity.Trip;
import com.gabrielluciu.travelplanner.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TripRepository extends JpaRepository<Trip, UUID> {
    List<Trip> findByUser(User user);

    Optional<Trip> findByIdAndUserId(UUID tripId, UUID userId);
}
