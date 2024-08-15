package com.myboard.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myboard.userservice.entity.Board;

@Repository
public interface BoardRepository extends MongoRepository<Board, Long> {

}
