package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomBoardRepository {
    Page<Board> findBoardsWithFilters(String searchText, LocalDate startDate, LocalDate endDate, String status, Boolean isRecent, Boolean isFavorite, List<String> boardIds, Pageable pageable);
}
