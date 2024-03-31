package com.myboard.userservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myboard.userservice.entity.DisplayTimeSlot;
import com.myboard.userservice.entity.User;

@Repository
public interface DisplayTimeSlotRepository extends MongoRepository<DisplayTimeSlot, String> {
	List<DisplayTimeSlot> findByDisplayOwnerUser(User user);

	List<DisplayTimeSlot> findByBoardOwnerUser(User user);

}
