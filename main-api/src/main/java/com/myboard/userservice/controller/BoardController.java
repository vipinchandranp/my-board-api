package com.myboard.userservice.controller;

import com.myboard.userservice.controller.apimodel.*;
import com.myboard.userservice.exception.MyBoardException;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.service.BoardService;
import com.myboard.userservice.service.UserService;
import com.myboard.userservice.types.APIType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private WorkFlow flow;

    @PostMapping("/save")
    public MainResponse signup(@RequestBody BoardSaveRequest boardSaveRequest) throws MyBoardException {
        boardService.process(boardSaveRequest, APIType.BOARD_SAVE);
        return new MainResponse<>(flow);
    }

    @PostMapping("/update")
    public MainResponse signup(@RequestBody BoardUpdateRequest boardSaveRequest) throws MyBoardException {
        boardService.process(boardSaveRequest, APIType.BOARD_UPDATE);
        return new MainResponse<>(flow);
    }

    @GetMapping("/delete")
    public MainResponse signup(@RequestParam Long id) throws MyBoardException {
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest();
        boardDeleteRequest.setId(id);
        boardService.process(boardDeleteRequest, APIType.BOARD_DELETE);
        return new MainResponse<>(flow);
    }
}
