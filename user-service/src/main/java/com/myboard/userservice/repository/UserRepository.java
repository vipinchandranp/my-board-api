package com.myboard.userservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.myboard.userservice.entity.User;

public interface UserRepository extends MongoRepository<User, String> {
	User findByUsername(String username);

	@Query("{'username': {$regex : ?0, $options: 'i'}}")
	List<User> findByUsernameContainingIgnoreCase(String pattern);
}
