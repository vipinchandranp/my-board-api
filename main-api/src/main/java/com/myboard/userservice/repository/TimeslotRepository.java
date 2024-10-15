package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Timeslot;
import com.myboard.userservice.entity.Display; // Import the Display entity
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeslotRepository extends MongoRepository<Timeslot, String> {

    // Find all timeslots for a specific display by ID
    List<Timeslot> findByDisplayId(String displayId);

    // Find all timeslots for multiple display IDs
    List<Timeslot> findByDisplayIdIn(List<String> displayIds);

    // Custom query to fetch timeslots using multiple display._id
    @Query("{'display.$id': {$in: ?0}}")
    List<Timeslot> findByDisplayIds(List<String> displayIds);

    // Find all timeslots associated with a specific Display entity
    List<Timeslot> findByDisplay(Display display);

    // Find all timeslots for a specific board
    List<Timeslot> findByBoardId(String boardId);

    // Optional: Find all timeslots for a specific display and board
    List<Timeslot> findByDisplayIdAndBoardId(String displayId, String boardId);

    // New method to find all timeslots associated with a list of Display entities
    List<Timeslot> findByDisplayIn(List<Display> displays);

    List<Timeslot> findByDisplayIdAndStartTimeBetween(String displayId, LocalDateTime startTime, LocalDateTime endTime);
}
