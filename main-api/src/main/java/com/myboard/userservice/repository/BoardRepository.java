package com.myboard.userservice.repository;

import com.myboard.userservice.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myboard.userservice.entity.Board;

import java.util.List;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
    boolean existsByName(String name);
    List<Board> findByCreatedBy(User user);
}
