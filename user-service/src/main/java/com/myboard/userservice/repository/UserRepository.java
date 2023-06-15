package com.myboard.userservice.repository;

import org.springframework.stereotype.Repository;

import com.myboard.userservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	// Define custom methods for user data access
	// ...
}
