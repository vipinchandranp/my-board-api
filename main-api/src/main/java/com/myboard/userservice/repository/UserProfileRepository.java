package com.myboard.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myboard.userservice.entity.UserProfile;

public interface UserProfileRepository extends MongoRepository<UserProfile, Long> {
	// You can define custom methods for UserProfile entities if needed
}
