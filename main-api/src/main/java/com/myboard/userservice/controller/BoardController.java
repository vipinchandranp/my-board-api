package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.board.request.BoardGetBoardsRequest;
import com.myboard.userservice.controller.model.board.response.BoardGetBoardsResponse;
import com.myboard.userservice.controller.model.board.response.BoardGetDisplayIdsResponse;
import com.myboard.userservice.controller.model.common.CommentRequest;
import com.myboard.userservice.controller.model.common.CommentResponse;
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



    @PostMapping("/{boardId}/rating")
    public MainResponse<String> addRating(@PathVariable String boardId, @RequestParam double rating) throws MBException {
        // Call the service layer to add the rating to the board
        boardService.addRating(boardId, rating);
        return buildResponse("Rating added successfully");
    }

    @GetMapping("/{boardId}/rating")
    public MainResponse<Double> getRating(@PathVariable String boardId) throws MBException {
        // Call the service to get the rating of the display
        Double rating = boardService.getRating(boardId);
        // Return the rating wrapped in the MainResponse
        return buildResponse(rating);
    }

    // Modified controller to accept comment text in request body
    @PostMapping("/{boardId}/comment")
    public MainResponse<String> addComment(
            @PathVariable String boardId,
            @RequestBody CommentRequest commentRequest) throws MBException {

        // Access the comment text from the request body
        String commentText = commentRequest.getCommentText();

        // Call service to add the comment (using displayId and commentText)
        String commentId = boardService.addComment(boardId, commentText);

        return buildResponse(commentId);  // Return the response with the comment ID
    }


    @GetMapping("/{boardId}/comments")
    public MainResponse<List<CommentResponse>> getCommentsForDisplay(@PathVariable String boardId) throws MBException {
        // Fetch comments with user profile details from the service
        List<CommentResponse> comments = boardService.getBoardComments(boardId);
        // Return the response encapsulated in MainResponse
        return new MainResponse<>(comments);
    }

    // Endpoint to like a display
    @PostMapping("/like/{boardId}")
    public MainResponse<String> likeBoard(@PathVariable String boardId) throws MBException {
        // Call the service layer to handle the "like" action
        boardService.likeBoard(boardId);
        return buildResponse("Display liked successfully");
    }

    // Endpoint to dislike a display
    @PostMapping("/dislike/{boardId}")
    public MainResponse<String> dislikeDisplay(@PathVariable String boardId) throws MBException {
        // Call the service layer to handle the "dislike" action
        boardService.dislikeBoard(boardId);
        return buildResponse("Board disliked successfully");
    }
    @PostMapping("/undo-like/{boardId}")
    public MainResponse<String> undoLikeBoard(@PathVariable String boardId) throws MBException {
        // Call the service layer to handle the "undo like" action
        boardService.undoLikeBoard(boardId);
        return buildResponse("Like removed successfully");
    }

    // Endpoint to undo a dislike on a display
    @PostMapping("/undo-dislike/{boardId}")
    public MainResponse<String> undoDislikeBoard(@PathVariable String boardId) throws MBException {
        // Call the service layer to handle the "undo dislike" action
        boardService.undoDislikeBoard(boardId);
        return buildResponse("Dislike removed successfully");
    }



}
