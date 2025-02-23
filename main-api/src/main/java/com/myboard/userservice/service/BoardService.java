package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.board.request.BoardApprovalRequest;
import com.myboard.userservice.controller.model.board.request.BoardGetRequest;
import com.myboard.userservice.controller.model.board.response.BoardGetBoardsByIdResponse;
import com.myboard.userservice.controller.model.common.AbstractFilterRequest;
import com.myboard.userservice.controller.model.common.CommentResponse;
import com.myboard.userservice.controller.model.common.MediaFile;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.entity.*;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.repository.*;
import com.myboard.userservice.types.ItemType;
import com.myboard.userservice.types.MediaType;
import com.myboard.userservice.types.NotificationType;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
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

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MongoRepository<Board, String> boardRepositoryWithFilter;

    @Autowired
    private CommonFilteredMongoRepository<Board> filteredMongoRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private CommentRepository commentRepository;

    public void addRating(String boardId, double ratingValue) throws MBException {
        // Fetch the board by its ID
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MBException("Board not found"));

        // Get the logged-in user (assumes you have a service for fetching user details)
        User loggedInUser = mbUserDetailsService.getLoggedInUser();

        // Check if the user has already rated this board
        Rating existingRating = board.getRatings().stream()
                .filter(rating -> rating != null && rating.getRatedBy() != null && rating.getRatedBy().getId().equals(loggedInUser.getId()))
                .findFirst()
                .orElse(null);

        // If the user has already rated, update the existing rating
        if (existingRating != null) {
            // Remove the old rating from the board's ratings list
            board.getRatings().remove(existingRating);

            // Update the existing rating's value and timestamp
            existingRating.setValue(ratingValue);
            existingRating.setTimestamp(System.currentTimeMillis());

            // Save the updated rating to the ratings table (repository)
            ratingRepository.save(existingRating);

            // Add the updated rating back to the board's ratings list
            board.getRatings().add(existingRating);
        } else {
            // If no existing rating, create a new rating
            Rating newRating = Rating.builder()
                    .itemType(ItemType.BOARD)    // Set the itemType to BOARD
                    .itemID(boardId)             // Set the itemID to the board's ID
                    .ratedBy(loggedInUser)       // Set the ratedBy field to the logged-in user object
                    .value(ratingValue)          // Set the rating value
                    .timestamp(System.currentTimeMillis())  // Set the timestamp of the rating
                    .build();

            // Save the new rating to the ratings table (repository)
            ratingRepository.save(newRating);

            // Add the new rating to the board's ratings list
            board.getRatings().add(newRating);
        }

        // Save the updated board with the new/updated rating
        boardRepository.save(board);

        // Add information to the flow (assuming 'flow' is a logger or some information store)
        flow.addInfo("Rating added successfully to the board");
    }

    public String addComment(String boardId, String commentText) throws MBException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MBException("Board not found"));

        User loggedInUser = mbUserDetailsService.getLoggedInUser();

        Comment comment = Comment.builder()
                .commentedBy(loggedInUser.getId())
                .content(commentText)
                .itemID(boardId)
                .itemType(ItemType.DISPLAY)
                .build();

        commentRepository.save(comment);

        board.getComments().add(comment);
        boardRepository.save(board);
        return comment.getId();
    }


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
                newBoard.setCreatedTime(LocalDateTime.now());
                newBoard.setMediaFiles(new ArrayList<>());
                return newBoard;
            });

            board.getMediaFiles().add(mediaFile);
            board.setModifiedBy(user);
            board.setLastModifiedTime(LocalDateTime.now());
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


    public Page<Board> getFilteredBoards(AbstractFilterRequest filterRequest, Pageable pageable) {
        User loggedInUser = mbUserDetailsService.getLoggedInUser();
        filterRequest.setCreatedBy(loggedInUser);
        Page<Board> boardPage = boardRepository.findAllByFilter(filterRequest, Board.class, pageable);

        // Update transient properties for each board (assuming Board has updateUserReaction)
        boardPage.forEach(board -> board.updateUserReaction(loggedInUser));

        return boardPage;
    }

    private Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size);
    }

    public void getBoardById(String boardId) throws MBException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MBException("Board not found"));

        // Convert media file paths to MediaFile objects
        List<MediaFile> mediaFiles = board.getMediaFiles();

        flow.setData(new BoardGetBoardsByIdResponse(
                board.getId(),
                board.getName(),
                board.getCreatedTime(),
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

    public void updateBoardStatus(String boardId, StatusType newStatus) throws MBException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MBException("Board not found"));

        StatusType oldStatus = board.getStatus();
        board.setStatus(newStatus);
        boardRepository.save(board);

        // Create and save a notification after status update
        createNotification(oldStatus, newStatus, boardId);

        flow.addInfo("Board status updated successfully");
    }

    private void createNotification(StatusType oldStatus, StatusType newStatus, String boardId) {
        Notification notification = new Notification();
        notification.setNotificationType(NotificationType.BOARD_STATUS_CHANGED);
        notificationRepository.save(notification);
    }


    public List<CommentResponse> getBoardComments(String boardId) throws MBException {

        User user = mbUserDetailsService.getLoggedInUser();

        // Fetch the display from the repository
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MBException("Board not found"));

        List<Comment> comments = board.getComments()
                .stream()
                .sorted(Comparator.comparing(Comment::getCreatedTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();


        // Transform comments into the response format with profile pictures
        return comments.stream().map(comment -> {
            return new CommentResponse(
                    comment.getId(),
                    comment.getContent(),
                    comment.getCreatedTime(),
                    user.getProfilePicName(),
                    user.getUsername()
            );
        }).collect(Collectors.toList());
    }

    public void likeBoard(String boardId) throws MBException {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new MBException("Board not found"));
        User user = mbUserDetailsService.getLoggedInUser();

        board.addLike(user);
        boardRepository.save(board);
    }

    public void dislikeBoard(String boardId) throws MBException {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new MBException("Board not found"));
        User user = mbUserDetailsService.getLoggedInUser();

        board.addDislike(user);
        boardRepository.save(board);
    }

    public void undoLikeBoard(String boardId) throws MBException {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new MBException("Board not found"));
        User user = mbUserDetailsService.getLoggedInUser();

        board.removeLike(user);
        boardRepository.save(board);
    }

    public void undoDislikeBoard(String boardId) throws MBException {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new MBException("Board not found"));
        User user = mbUserDetailsService.getLoggedInUser();

        board.removeDislike(user);
        boardRepository.save(board);
    }

    public Double getRating(String boardId) throws MBException {
        // Fetch the board entity from the database
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new MBException("Board not found"));

        // Return the rating associated with this display
        // Assuming board has a 'getRating' method or a ratings field
        return board.getAverageRating();  // or calculate the average rating if you have multiple ratings per display
    }


    public Integer getNumberOfLikes(String boardId) throws MBException {
        // Fetch the display entity from the database
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MBException("Board not found"));

        // Assuming display.getLikes() returns a collection of likes
        return board.getLikeCount();
    }

    public Integer getNumberOfDisLikes(String boardId) throws MBException {
        // Fetch the display entity from the database
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MBException("Board not found"));

        // Assuming display.getLikes() returns a collection of likes
        return board.getDislikeCount();
    }


}
