package com.myboard.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myboard.userservice.entity.Display;

public interface DisplayRepository extends MongoRepository<Display, String> {
	// You can add custom query methods if needed
}