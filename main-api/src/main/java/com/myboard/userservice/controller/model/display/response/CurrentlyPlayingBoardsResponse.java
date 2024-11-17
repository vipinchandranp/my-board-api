package com.myboard.userservice.controller.model.display.response;

import lombok.Data;
import java.util.List;

@Data
public class CurrentlyPlayingBoardsResponse {
    private List<String> currentlyPlaying;
    private List<String> previouslyPlayed;
    private List<String> upcoming;
}
