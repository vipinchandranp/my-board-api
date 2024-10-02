package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Update the repository to include pagination
@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
    boolean existsByNameAndCreatedBy(String name, User createdBy);
    List<Board> findByCreatedBy(User user);
    Optional<Board> findByName(String boardName);
    Page<Board> findAll(Pageable pageable); // Keep the default pagination method
}
