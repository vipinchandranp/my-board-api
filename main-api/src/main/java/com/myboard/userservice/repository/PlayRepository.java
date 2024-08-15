package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Content;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayRepository extends MongoRepository<Content, Long> {
}