package com.myboard.userservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myboard.userservice.entity.DisplayTimeSlot;
import com.myboard.userservice.entity.User;

public interface DateTimeSlotRepository extends MongoRepository<DisplayTimeSlot, String> {
	List<DisplayTimeSlot> findByDisplayOwnerUser(User user);

	List<DisplayTimeSlot> findByBoardOwnerUser(User user);

}
