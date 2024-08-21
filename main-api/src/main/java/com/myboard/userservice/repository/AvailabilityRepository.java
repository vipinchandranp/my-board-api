package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityRepository extends MongoRepository<Availability, String> {

    // Find availability by displayId
    List<Availability> findByDisplayId(Long displayId);

    // Find availability by displayId and date
    Availability findByDisplayIdAndDate(Long displayId, LocalDate date);

    // Optional: Find all availability records for a given displayId within a date range
    List<Availability> findByDisplayIdAndDateBetween(String displayId, LocalDate startDate, LocalDate endDate);
}
