package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.board.*;
import com.myboard.userservice.controller.model.common.MainRequest;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.Display;
import com.myboard.userservice.entity.Media;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.types.APIType;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

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
    private DisplayRepository displayRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MBUserDetailsService mbUserDetailsService;

    @Value("${myboard.board.path}")
    private String boardPath;

    public void process(MainRequest baseRequest, APIType apiType) throws MBException {
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
                case BOARD_APPROVAL:
                    handleBoardApproval((BoardApprovalRequest) baseRequest);
                    break;
                default:
                    throw new MBException("Invalid API type");
            }
        } catch (Exception e) {
            throw new MBException(e, e.getMessage());
        }
    }

    private void handleBoardApproval(BoardApprovalRequest boardApprovalStatusRequest) {
        Board board = boardRepository.findById(boardApprovalStatusRequest.getBoardId()).orElse(null);
        if (board == null) {
            String message = messageSource.getMessage("board.update.failure", null, Locale.getDefault());
            throw new MBException(message);
        }
        if (boardApprovalStatusRequest.isApprove()) {
            board.setStatus(StatusType.APPROVED);
        } else {
            board.setStatus(StatusType.REJECTED);
        }
        boardRepository.save(board);
        String boardSaveMessage = null;
        if (board.getId() != null) {
            boardSaveMessage = "Board updated successfully";
        } else {
            boardSaveMessage = "Failed to update board";
        }
        flow.addInfo(boardSaveMessage);
    }

    private void handleBoardSave(BoardSaveRequest boardSaveRequest) throws MBException, IOException {
        // Check if the board already exists
        if (boardRepository.existsByName(boardSaveRequest.getName())) {
            flow.addError(messageSource.getMessage("board.save.failure", null, Locale.getDefault()));
            throw new MBException();
        }
        User loggedInUser = mbUserDetailsService.getLoggedInUser();
        MultipartFile mediaContent = boardSaveRequest.getMediaContent();
        Media media = Media.builder()
                .mediaType(MediaType.IMAGE)
                .mediaLocation(boardPath + "/" + loggedInUser)
                .mediaName(Optional.ofNullable(mediaContent).map(MultipartFile::getName).orElse(null))
                .mediaContent(Optional.ofNullable(mediaContent).map(m -> {
                    try {
                        return m.getBytes();
                    } catch (IOException e) {
                        // Handle exception and provide default content
                        return new byte[0];
                    }
                }).orElse(new byte[0]))
                .build();
        Board board = Board.builder()
                .name(boardSaveRequest.getName())
                .media(media).build();
        boardRepository.save(board);
        // Prepare the response
        String boardSaveMessage = null;
        if (board.getId() != null) {
            boardSaveMessage = messageSource.getMessage("board.save.success", null, Locale.getDefault());
        } else {
            boardSaveMessage = messageSource.getMessage("board.save.failure", null, Locale.getDefault());
        }
        flow.addInfo(boardSaveMessage);
    }

    private void handleBoardUpdate(BoardUpdateRequest boardUpdateRequest) throws MBException, IOException {
        Board board = boardRepository.findById(boardUpdateRequest.getBoardId()).orElse(null);
        if (board == null) {
            String message = messageSource.getMessage("board.update.failure", null, Locale.getDefault());
            throw new MBException(message);
        }
        User loggedInUser = mbUserDetailsService.getLoggedInUser();
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
        if (board.getId() != null) {
            boardSaveMessage = messageSource.getMessage("board.update.success", null, Locale.getDefault());
        } else {
            boardSaveMessage = messageSource.getMessage("board.update.failure", null, Locale.getDefault());
        }
        flow.addInfo(boardSaveMessage);
    }

    private void handleBoardDelete(BoardDeleteRequest boardDeleteRequest) throws MBException, IOException {
        try {
            boardRepository.deleteById(boardDeleteRequest.getBoardId());
        } catch (Exception e) {
            String message = messageSource.getMessage("board.delete.failure", null, Locale.getDefault());
            throw new MBException(message);
        }
        String boardSaveMessage = messageSource.getMessage("board.delete.success", null, Locale.getDefault());
        flow.addInfo(boardSaveMessage);
    }

    private void handleBoardGet(BoardGetRequest boardGetRequest) throws MBException, IOException {
        Board board = boardRepository.findById(boardGetRequest.getBoardId()).orElse(null);
        if (board == null) {
            String message = messageSource.getMessage("board.get.failure", null, Locale.getDefault());
            throw new MBException(message);
        }
        flow.setData(board);
    }
}
