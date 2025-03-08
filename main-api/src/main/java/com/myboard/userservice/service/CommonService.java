package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.common.AbstractFilterRequest;
import com.myboard.userservice.controller.model.common.AbstractFilterResponse;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.Display;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.types.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommonService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private DisplayRepository displayRepository;

    public List<AbstractFilterResponse> getCommonItems(AbstractFilterRequest filterRequest) {
        // Pagination settings
        PageRequest pageRequest = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());

        // Fetch boards filtered by criteria
        Page<Board> boardsPage = boardRepository.findAllByFilter(filterRequest, Board.class, pageRequest);
        List<AbstractFilterResponse> boardResponses = boardsPage.stream()
                .map(board -> AbstractFilterResponse.builder()
                        .id(((Board) board).getId())
                        .name(((Board) board).getName()) // Assuming Board has a getName() method
                        .itemType(ItemType.BOARD)
                        .build())
                .collect(Collectors.toList());

        // Fetch displays filtered by criteria
        Page<Display> displaysPage = displayRepository.findAllByFilter(filterRequest, Display.class, pageRequest);
        List<AbstractFilterResponse> displayResponses = displaysPage.stream()
                .map(display -> AbstractFilterResponse.builder()
                        .id(((Display) display).getId())
                        .name(((Display) display).getName()) // Assuming Display has a getName() method
                        .itemType(ItemType.DISPLAY)
                        .build())
                .collect(Collectors.toList());

        // Combine results
        List<AbstractFilterResponse> combinedResponse = new ArrayList<>();
        combinedResponse.addAll(boardResponses);
        combinedResponse.addAll(displayResponses);

        return combinedResponse;
    }
}
