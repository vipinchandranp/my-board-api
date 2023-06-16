package com.myboard.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myboard.userservice.entity.User;

public interface UserRepository extends MongoRepository<User, String> {
	User findByUsername(String username);
}
