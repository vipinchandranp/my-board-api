package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Timeslot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeslotRepository extends MongoRepository<Timeslot, String> {
    
    // Find all timeslots for a specific display
    List<Timeslot> findByDisplayId(String displayId);

    // Find all timeslots for a specific board
    List<Timeslot> findByBoardId(String boardId);
    
    // Optional: Find all timeslots for a specific display and board
    List<Timeslot> findByDisplayIdAndBoardId(String displayId, String boardId);
    
    // You can also add more custom query methods as needed
}
