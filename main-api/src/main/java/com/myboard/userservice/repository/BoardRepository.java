package com.myboard.userservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.myboard.userservice.entity.Board;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {

	List<Board> findByUserId(String userId);

	Page<Board> findByUserId(String userId, Pageable pageable);

	@Query(value = "{ 'userId': ?0 }", fields = "{ 'id': 1, 'userId': 1, 'title': 1 }")
	List<ProjectionTitleIdDto> getTitleAndId(String loggedInUser);

	// Define a ProjectionDto class to hold the result fields
	interface ProjectionTitleIdDto {
		String getId();

		String getUserId();

		String getTitle();
	}
}
