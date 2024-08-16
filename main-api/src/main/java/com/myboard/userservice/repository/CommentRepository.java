package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Comment;
import com.myboard.userservice.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {
}