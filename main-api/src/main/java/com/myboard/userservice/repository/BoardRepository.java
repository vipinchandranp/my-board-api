package com.myboard.userservice.repository;

import com.myboard.userservice.controller.model.board.request.BoardGetBoardsRequest;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends MongoRepository<Board, String>, CommonFilteredMongoRepository<Board> {

    boolean existsByNameAndCreatedBy(String name, User createdBy);

    List<Board> findByCreatedBy(User user);

    Optional<Board> findByName(String boardName);

    Page<Board> findAll(Pageable pageable); // Default pagination method

}
