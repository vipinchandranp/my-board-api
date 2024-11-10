package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.board.request.BoardGetBoardsRequest;
import com.myboard.userservice.controller.model.board.response.BoardGetBoardsResponse;
import com.myboard.userservice.controller.model.board.response.BoardGetDisplayIdsResponse;
import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.display.response.DisplayGetBoardIdsResponse;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.service.BoardService;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/board")
public class BoardController extends BaseController {

    @Autowired
    private BoardService boardService;

    @PostMapping("/media/save")
    public MainResponse<String> saveBoard(@RequestParam("file") MultipartFile file, @RequestParam String boardName) throws MBException, IOException {
        boardService.saveBoard(file, boardName);
        return buildResponse();
    }

    @PutMapping("/media/add/{boardId}")
    public MainResponse<String> addMedia(@PathVariable String boardId, @RequestParam("file") MultipartFile file) throws MBException, IOException {
        boardService.addMedia(boardId, file);
        return buildResponse();
    }

    @DeleteMapping("/media/delete/{boardId}/{mediaName}")
    public MainResponse<String> deleteMedia(@PathVariable String boardId, @PathVariable String mediaName) throws MBException {
        boardService.deleteMedia(boardId, mediaName);
        return buildResponse();
    }

    @DeleteMapping("/delete/{boardId}")
    public MainResponse<String> deleteBoard(@PathVariable String boardId) throws MBException {
        boardService.deleteBoard(boardId);
        return buildResponse();
    }

    @GetMapping("/list")
    public MainResponse<List<BoardGetBoardsResponse>> getBoards(BoardGetBoardsRequest request) throws MBException {
        // Get the paginated and filtered list of boards from the service
        Page<Board> boardsPage = boardService.getFilteredBoards(request, PageRequest.of(request.getPage(), request.getSize()));

        // Map the Page<Board> to the response model
        List<BoardGetBoardsResponse> boardResponses = boardsPage.getContent().stream()
                .map(board -> new BoardGetBoardsResponse(board)) // Assuming BoardGetBoardsResponse has a constructor that takes a Board entity
                .collect(Collectors.toList());

        // Build and return the response with pagination data
        return buildResponse(boardResponses, boardsPage.getTotalElements(), boardsPage.getTotalPages(), boardsPage.getNumber());
    }

    @GetMapping("/{boardId}")
    public MainResponse<BoardGetBoardsResponse> getBoardById(@PathVariable String boardId) throws MBException {
        boardService.getBoardById(boardId);
        return buildResponse();
    }

    @GetMapping("/display/{boardId}")
    public MainResponse<BoardGetDisplayIdsResponse> getBoardIdsByDisplayId(@PathVariable String boardId) throws MBException {
        boardService.getDisplayIdsByBoardId(boardId);
        return buildResponse();
    }

    // New endpoint to update board status
    @PutMapping("/update-status/{boardId}")
    public MainResponse<String> updateBoardStatus(@PathVariable String boardId, @RequestParam StatusType newStatus) throws MBException {
        boardService.updateBoardStatus(boardId, newStatus);
        return buildResponse("Board status updated successfully");
    }
}
