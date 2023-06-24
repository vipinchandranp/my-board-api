package com.myboard.userservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myboard.userservice.entity.Board;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
	List<Board> findByUserId(String userId);
}
