package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.board.request.BoardApprovalRequest;
import com.myboard.userservice.controller.model.board.request.BoardGetBoardsRequest;
import com.myboard.userservice.controller.model.board.request.BoardGetRequest;
import com.myboard.userservice.controller.model.board.response.BoardGetBoardsByIdResponse;
import com.myboard.userservice.controller.model.board.response.BoardGetBoardsResponse;
import com.myboard.userservice.controller.model.common.MediaFile;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.Display;
import com.myboard.userservice.entity.Timeslot;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.repository.*;
import com.myboard.userservice.types.MediaType;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkFlow flow;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CustomBoardRepository customBoardRepository;

    @Autowired
    private DisplayRepository displayRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MBUserDetailsService mbUserDetailsService;

    @Value("${myboard.board.path}")
    private String boardPath;

    @Autowired
    private UtilService utilService;

    @Autowired
    private TimeslotRepository timeslotRepository;

    public void approveBoard(BoardApprovalRequest boardApprovalStatusRequest) {
        Board board = boardRepository.findById(boardApprovalStatusRequest.getBoardId()).orElse(null);
        if (board == null) {
            String message = messageSource.getMessage("board.update.failure", null, Locale.getDefault());
            throw new MBException(message);
        }
        board.setStatus(boardApprovalStatusRequest.isApprove() ? StatusType.APPROVED : StatusType.REJECTED);
        boardRepository.save(board);
        flow.addInfo("Board updated successfully");
    }

    public void getBoard(BoardGetRequest boardGetRequest) throws MBException {
        Board board = boardRepository.findById(boardGetRequest.getBoardId()).orElse(null);
        if (board == null) {
            String message = messageSource.getMessage("board.get.failure", null, Locale.getDefault());
            throw new MBException(message);
        }
        flow.setData(board);
    }

    public void saveBoard(MultipartFile file, String boardName) throws MBException {
        if (file.isEmpty()) {
            throw new MBException("File is empty");
        }
        try {
            User user = mbUserDetailsService.getLoggedInUser();
            String username = user.getUsername();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(boardPath, username, uniqueFileName);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            // Use UtilService to determine the media type
            MediaType mediaType = utilService.determineMediaType(file);
            MediaFile mediaFile = new MediaFile("http://192.168.1.43:8080/myboard/file/board/" + uniqueFileName, mediaType);

            Board board = (Board) boardRepository.findByName(boardName).orElseGet(() -> {
                Board newBoard = new Board();
                newBoard.setName(boardName);
                newBoard.setCreatedBy(user);
                newBoard.setCreatedAt(LocalDateTime.now());
                newBoard.setMediaFiles(new ArrayList<>());
                return newBoard;
            });

            board.getMediaFiles().add(mediaFile);
            board.setModifiedBy(user);
            board.setLastModifiedAt(LocalDateTime.now());
            boardRepository.save(board);
            flow.setData(Map.of("boardId", board.getId(), "fileName", uniqueFileName));

        } catch (IOException e) {
            throw new MBException("Failed to save file", e);
        }
    }

    public void addMedia(String boardId, MultipartFile file) throws MBException {
        if (file.isEmpty()) {
            throw new MBException("File is empty");
        }
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {
            throw new MBException("Board not found");
        }
        try {
            User loggedInUser = mbUserDetailsService.getLoggedInUser();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path userDirectory = Paths.get(boardPath, loggedInUser.getId().toString());
            Files.createDirectories(userDirectory);
            Path filePath = userDirectory.resolve(uniqueFileName);
            Files.write(filePath, file.getBytes());

            MediaType mediaType = utilService.determineMediaType(file);
            MediaFile mediaFile = new MediaFile("http://192.168.1.43:8080/myboard/file/board/" + uniqueFileName, mediaType);
            board.getMediaFiles().add(mediaFile);
            boardRepository.save(board);
            flow.setData(Map.of("boardId", board.getId(), "fileName", uniqueFileName));
            flow.addInfo("Media added successfully");
        } catch (IOException e) {
            throw new MBException("Failed to save media", e);
        }
    }

    public void deleteBoard(String boardId) throws MBException {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {
            throw new MBException("Board not found");
        }
        try {
            User loggedInUser = mbUserDetailsService.getLoggedInUser();
            for (MediaFile mediaFile : board.getMediaFiles()) {
                Path mediaPath = Paths.get(boardPath, loggedInUser.getId().toString(), mediaFile.getFileName());
                Files.deleteIfExists(mediaPath);
            }
            boardRepository.deleteById(boardId);
            flow.addInfo("Board deleted successfully");
        } catch (IOException e) {
            throw new MBException("Failed to delete board", e);
        }
    }

    public void deleteMedia(String boardId, String mediaName) throws MBException {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {
            throw new MBException("Board not found");
        }

        List<MediaFile> mediaFiles = board.getMediaFiles().stream()
                .filter(mediaFile -> mediaFile.getFileName().equals(mediaName))
                .collect(Collectors.toList());

        if (mediaFiles.isEmpty()) {
            throw new MBException("Media not found on the board");
        }

        try {
            User loggedInUser = mbUserDetailsService.getLoggedInUser();
            Path mediaPath = Paths.get(boardPath, loggedInUser.getId().toString(), mediaName);
            Files.deleteIfExists(mediaPath);

            board.getMediaFiles().removeIf(mediaFile -> mediaFile.getFileName().equals(mediaName));
            boardRepository.save(board);
            flow.addInfo("Media deleted successfully");
        } catch (IOException e) {
            throw new MBException("Failed to delete media", e);
        }
    }

    public List<BoardGetBoardsResponse> getBoards(BoardGetBoardsRequest request) throws MBException {
        // Extract pagination parameters
        int page = request.getPage();
        int size = request.getSize();
        Pageable pageable = PageRequest.of(page, size);

        // Use the custom repository method to fetch filtered and paginated boards
        Page<Board> boardPage = customBoardRepository.findBoardsWithFilters(
                request.getSearchText(),
                request.getStartDate(),
                request.getEndDate(),
                request.getStatus(),
                request.getIsRecent(),
                request.getIsFavorite(),
                request.getBoardIds(),
                pageable
        );

        // Convert the boards to response DTOs
        List<BoardGetBoardsResponse> boards = boardPage.getContent().stream()
                .map(board -> new BoardGetBoardsResponse(
                        board.getId(),
                        board.getName(),
                        board.getMediaFiles(),
                        board.getCreatedAt(),
                        board.getStatus().toString()
                ))
                .collect(Collectors.toList());

        // Set data and add info to the flow (for logging or tracking)
        flow.setData(boards);
        flow.addInfo("Boards fetched successfully");

        return boards;
    }


    public void getBoardById(String boardId) throws MBException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MBException("Board not found"));

        // Convert media file paths to MediaFile objects
        List<MediaFile> mediaFiles = board.getMediaFiles();

        flow.setData(new BoardGetBoardsByIdResponse(
                board.getId(),
                board.getName(),
                board.getCreatedAt(),
                board.getStatus().name(),
                mediaFiles // Pass the list of MediaFile objects
        ));
    }

    public List<String> getDisplayIdsByBoardId(String boardId) throws MBException {
        // Validate the display
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {
            throw new MBException("Board not found");
        }

        // Retrieve all timeslots for the display
        List<Timeslot> timeslots = timeslotRepository.findByBoardId(boardId);

        // Extract the board IDs from the timeslots
        List<String> displayIds = timeslots.stream().map(timeslot -> timeslot.getDisplay().getId()).distinct()
                .collect(Collectors.toList());
        flow.setData(displayIds);
        return displayIds;
    }

}
