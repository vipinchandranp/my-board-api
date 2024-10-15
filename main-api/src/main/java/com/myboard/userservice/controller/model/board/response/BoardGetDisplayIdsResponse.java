// DisplayGetBoardIdsResponse.java
package com.myboard.userservice.controller.model.board.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BoardGetDisplayIdsResponse {
    private String boardId;
    private List<String> displayIds;
}
