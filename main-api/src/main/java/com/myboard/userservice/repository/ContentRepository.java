package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Comment;
import com.myboard.userservice.entity.Content;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContentRepository extends MongoRepository<Content, String> {
}