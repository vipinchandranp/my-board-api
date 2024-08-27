package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.myboard.userservice.entity.Display;

import java.util.List;

public interface DisplayRepository extends MongoRepository<Display, String> {
    boolean existsByName(String name);
    List<Display> findByCreatedBy(User user);
}