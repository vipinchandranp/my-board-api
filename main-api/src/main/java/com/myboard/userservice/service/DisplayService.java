package com.myboard.userservice.service;

import com.myboard.userservice.controller.apimodel.*;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.Display;
import com.myboard.userservice.entity.Media;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.exception.MyBoardException;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.types.APIType;
import com.myboard.userservice.types.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;

@Service
public class DisplayService {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkFlow flow;

    @Autowired
    private DisplayRepository displayRepository;

    @Autowired
    private MessageSource messageSource;

    @Value("${myboard.board.path}")
    private String boardPath;

    public void process(MainRequest baseRequest, APIType apiType) throws MyBoardException {
        try {
            switch (apiType) {
                case DISPLAY_GET:
                    handleDisplayGet((DisplaySaveRequest) baseRequest);
                    break;
                case DISPLAY_SAVE:
                    handleDisplaySave((DisplaySaveRequest) baseRequest);
                    break;
                case DISPLAY_UPDATE:
                    handleDisplayUpdate((DisplayUpdateRequest) baseRequest);
                    break;
                case DISPLAY_DELETE:
                    handleDisplayDelete((DisplayDeleteRequest) baseRequest);
                    break;
                default:
                    throw new MyBoardException("Invalid API type");
            }
        } catch (Exception e) {
            throw new MyBoardException(e, e.getMessage());
        }
    }

    private void handleDisplaySave(DisplaySaveRequest displaySaveRequest) throws MyBoardException, IOException {
        // Check if the board already exists
        if (displayRepository.existsByName(displaySaveRequest.getName())) {
            flow.addError(messageSource.getMessage("display.save.failure", null, Locale.getDefault()));
            throw new MyBoardException();
        }
        User loggedInUser = userService.getLoggedInUser();
        MultipartFile mediaContent = displaySaveRequest.getMediaContent();
        Media media = Media.builder()
                .mediaType(MediaType.IMAGE)
                .mediaLocation(boardPath + "/" + loggedInUser)
                .mediaName(mediaContent.getName())
                .mediaContent(mediaContent.getBytes())
                .build();

        Display display = new Display(media);
        displayRepository.save(display);
        // Prepare the response
        String saveMessage = null;
        if (display.getId() > 0) {
            saveMessage = messageSource.getMessage("display.save.success", null, Locale.getDefault());
        } else {
            saveMessage = messageSource.getMessage("display.save.failure", null, Locale.getDefault());
        }

        flow.addInfo(saveMessage);
    }

    private void handleDisplayUpdate(DisplayUpdateRequest boardUpdateRequest) throws MyBoardException, IOException {
        Display display = displayRepository.findById(boardUpdateRequest.getId()).orElse(null);
        if (display == null) {
            String message = messageSource.getMessage("display.update.failure", null, Locale.getDefault());
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
        display.setMedia(media);
        displayRepository.save(display);
        // Prepare the response
        String boardSaveMessage = null;
        if (display.getId() > 0) {
            boardSaveMessage = messageSource.getMessage("display.update.success", null, Locale.getDefault());
        } else {
            boardSaveMessage = messageSource.getMessage("display.update.failure", null, Locale.getDefault());
        }
        flow.addInfo(boardSaveMessage);
    }

    private void handleDisplayDelete(DisplayDeleteRequest displayDeleteRequest) throws MyBoardException, IOException {
        try {
            displayRepository.deleteById(displayDeleteRequest.getId());
        } catch (Exception e) {
            String message = messageSource.getMessage("display.delete.failure", null, Locale.getDefault());
            throw new MyBoardException(message);
        }
        String displaySaveMessage = messageSource.getMessage("display.delete.success", null, Locale.getDefault());
        flow.addInfo(displaySaveMessage);
    }

    private void handleDisplayGet(DisplayGetRequest displayGetRequest) throws MyBoardException, IOException {
        Display display = displayRepository.findById(displayGetRequest.getId()).orElse(null);
        if (display == null) {
            String message = messageSource.getMessage("display.get.failure", null, Locale.getDefault());
            throw new MyBoardException(message);
        }
        flow.setData(display);
    }
}
