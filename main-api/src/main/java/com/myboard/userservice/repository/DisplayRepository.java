package com.myboard.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myboard.userservice.entity.DisplayDetails;

public interface DisplayRepository extends MongoRepository<DisplayDetails, String> {
	// You can add custom query methods if needed
}