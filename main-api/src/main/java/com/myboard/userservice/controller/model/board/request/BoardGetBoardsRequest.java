package com.myboard.userservice.controller.model.board.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class BoardGetBoardsRequest {
    private int page;
    private int size;
    private String searchText;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Boolean isRecent;
    private Boolean isFavorite;
    private List<String> boardIds;

}
