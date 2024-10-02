package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Board;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class BoardSpecifications {
    
    public static Specification<Board> hasNameLike(String name) {
        return (root, query, criteriaBuilder) -> 
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }
    
    public static Specification<Board> hasDateRange(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> 
                criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
    }

    public static Specification<Board> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Board> isRecent() {
        return (root, query, criteriaBuilder) -> 
                criteriaBuilder.greaterThan(root.get("createdAt"), LocalDate.now().minusDays(7));
    }

    public static Specification<Board> isFavorite() {
        return (root, query, criteriaBuilder) -> 
                criteriaBuilder.isTrue(root.get("isFavorite"));
    }

    public static Specification<Board> hasBoardIds(List<String> boardIds) {
        return (root, query, criteriaBuilder) -> 
                root.get("id").in(boardIds);
    }
}
