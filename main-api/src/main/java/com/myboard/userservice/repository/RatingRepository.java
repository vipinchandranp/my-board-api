package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RatingRepository extends MongoRepository<Comment, String> {
}