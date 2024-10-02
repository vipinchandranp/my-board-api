package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Board> findBoardsWithFilters(String searchText, LocalDate startDate, LocalDate endDate, String status, Boolean isRecent, Boolean isFavorite, List<String> boardIds, Pageable pageable) {
        Query query = new Query();
        
        // Add dynamic filters
        if (searchText != null && !searchText.isEmpty()) {
            query.addCriteria(Criteria.where("name").regex(searchText, "i")); // Case-insensitive search
        }

        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("createdAt").gte(startDate).lte(endDate));
        }

        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }

        if (Boolean.TRUE.equals(isRecent)) {
            query.addCriteria(Criteria.where("createdAt").gte(LocalDate.now().minusDays(7)));
        }

        if (Boolean.TRUE.equals(isFavorite)) {
            query.addCriteria(Criteria.where("isFavorite").is(true));
        }

        if (boardIds != null && !boardIds.isEmpty()) {
            query.addCriteria(Criteria.where("id").in(boardIds));
        }

        // Apply pagination
        long total = mongoTemplate.count(query, Board.class);
        query.with(pageable);

        // Execute the query
        List<Board> boards = mongoTemplate.find(query, Board.class);

        return new PageImpl<>(boards, pageable, total);
    }
}
