package com.myboard.userservice.service;

import com.myboard.userservice.controller.apimodel.*;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.Media;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.exception.MyBoardException;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.types.APIType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Locale;

import com.myboard.userservice.types.MediaType;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BoardService {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkFlow flow;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MessageSource messageSource;

    @Value("${myboard.board.path}")
    private String boardPath;

    public void process(MainRequest baseRequest, APIType apiType) throws MyBoardException {
        try {
            switch (apiType) {
                case BOARD_GET:
                    handleBoardGet((BoardGetRequest) baseRequest);
                    break;
                case BOARD_SAVE:
                    handleBoardSave((BoardSaveRequest) baseRequest);
                    break;
                case BOARD_UPDATE:
                    handleBoardUpdate((BoardUpdateRequest) baseRequest);
                    break;
                case BOARD_DELETE:
                    handleBoardDelete((BoardDeleteRequest) baseRequest);
                    break;
                default:
                    throw new MyBoardException("Invalid API type");
            }
        } catch (Exception e) {
            throw new MyBoardException(e, e.getMessage());
        }
    }

    private void handleBoardSave(BoardSaveRequest boardSaveRequest) throws MyBoardException, IOException {
        // Check if the board already exists
        if (boardRepository.existsByName(boardSaveRequest.getName())) {
            flow.addError(messageSource.getMessage("board.save.failure", null, Locale.getDefault()));
            throw new MyBoardException();
        }
        User loggedInUser = userService.getLoggedInUser();
        MultipartFile mediaContent = boardSaveRequest.getMediaContent();
        Media media = Media.builder()
                .mediaType(MediaType.IMAGE)
                .mediaLocation(boardPath + "/" + loggedInUser)
                .mediaName(mediaContent.getName())
                .mediaContent(mediaContent.getBytes())
                .build();
        Board board = new Board(media);
        boardRepository.save(board);
        // Prepare the response
        String boardSaveMessage = null;
        if (board.getId() > 0) {
            boardSaveMessage = messageSource.getMessage("board.save.success", null, Locale.getDefault());
        } else {
            boardSaveMessage = messageSource.getMessage("board.save.failure", null, Locale.getDefault());
        }
        flow.addInfo(boardSaveMessage);
    }

    private void handleBoardUpdate(BoardUpdateRequest boardUpdateRequest) throws MyBoardException, IOException {
        Board board = boardRepository.findById(boardUpdateRequest.getId()).orElse(null);
        if (board == null) {
            String message = messageSource.getMessage("board.update.failure", null, Locale.getDefault());
            throw new MyBoardException(message);
        }
        User loggedInUser = userService.getLoggedInUser();
        MultipartFile mediaContent = boardUpdateRequest.getMediaContent();
        Media media = Media.builder()
                .mediaType(MediaType.IMAGE)
                .mediaLocation(boardPath + "/" + loggedInUser)
                .mediaName(mediaContent.getName())
                .mediaContent(mediaContent.getBytes())
                .build();
        board.setMedia(media);
        boardRepository.save(board);
        // Prepare the response
        String boardSaveMessage = null;
        if (board.getId() > 0) {
            boardSaveMessage = messageSource.getMessage("board.update.success", null, Locale.getDefault());
        } else {
            boardSaveMessage = messageSource.getMessage("board.update.failure", null, Locale.getDefault());
        }
        flow.addInfo(boardSaveMessage);
    }

    private void handleBoardDelete(BoardDeleteRequest boardDeleteRequest) throws MyBoardException, IOException {
        try {
            boardRepository.deleteById(boardDeleteRequest.getId());
        } catch (Exception e) {
            String message = messageSource.getMessage("board.delete.failure", null, Locale.getDefault());
            throw new MyBoardException(message);
        }
        String boardSaveMessage = messageSource.getMessage("board.delete.success", null, Locale.getDefault());
        flow.addInfo(boardSaveMessage);
    }

    private void handleBoardGet(BoardGetRequest boardGetRequest) throws MyBoardException, IOException {
        Board board = boardRepository.findById(boardGetRequest.getId()).orElse(null);
        if (board == null) {
            String message = messageSource.getMessage("board.get.failure", null, Locale.getDefault());
            throw new MyBoardException(message);
        }
        flow.setData(board);
    }
}
