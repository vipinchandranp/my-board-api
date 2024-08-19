package com.myboard.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myboard.userservice.entity.Display;

public interface DisplayRepository extends MongoRepository<Display, Long> {
    boolean existsByName(String name);
}