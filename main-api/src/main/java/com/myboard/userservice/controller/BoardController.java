package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.*;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.service.BoardService;
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
    public MainResponse save(@RequestBody BoardSaveRequest boardSaveRequest) throws MBException {
        boardService.process(boardSaveRequest, APIType.BOARD_SAVE);
        return new MainResponse<>(flow);
    }

    @PostMapping("/update")
    public MainResponse update(@RequestBody BoardUpdateRequest boardSaveRequest) throws MBException {
        boardService.process(boardSaveRequest, APIType.BOARD_UPDATE);
        return new MainResponse<>(flow);
    }

    @GetMapping("/delete/{id}")
    public MainResponse delete(@PathVariable String id) throws MBException {
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest();
        boardDeleteRequest.setId(id);
        boardService.process(boardDeleteRequest, APIType.BOARD_DELETE);
        return new MainResponse<>(flow);
    }

    @GetMapping("/get/{id}")
    public MainResponse get(@PathVariable String id) throws MBException {
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest();
        boardDeleteRequest.setId(id);
        boardService.process(boardDeleteRequest, APIType.BOARD_GET);
        return new MainResponse<>(flow);
    }
}
