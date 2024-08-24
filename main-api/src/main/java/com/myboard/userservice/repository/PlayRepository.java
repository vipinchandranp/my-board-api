package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Play;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayRepository extends MongoRepository<Play, String> {
}