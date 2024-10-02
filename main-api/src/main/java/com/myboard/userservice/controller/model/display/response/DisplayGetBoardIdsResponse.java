// DisplayGetBoardIdsResponse.java
package com.myboard.userservice.controller.model.display.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DisplayGetBoardIdsResponse {
    private String displayId;
    private List<String> boardIds;
}
