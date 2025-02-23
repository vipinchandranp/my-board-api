package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Timeslot;
import com.myboard.userservice.entity.Display; // Import the Display entity
import com.myboard.userservice.entity.User;
import com.myboard.userservice.types.StatusType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeslotRepository extends MongoRepository<Timeslot, String> {

    // Find all timeslots for a specific display by ID
    List<Timeslot> findByDisplayId(String displayId);

    // Find all timeslots for a specific board
    List<Timeslot> findByBoardId(String boardId);

    // New method to find all timeslots associated with a list of Display entities
    List<Timeslot> findByDisplayIn(List<Display> displays);

    @Query("{ 'displayId': ?0, 'startTime': { $lte: ?2 }, 'endTime': { $gte: ?1 } }")
    List<Timeslot> findByDisplayAndTimeRange(String displayId, Instant startTime, Instant endTime);
}
