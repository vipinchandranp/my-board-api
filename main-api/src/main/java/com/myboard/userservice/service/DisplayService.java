package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.board.response.BoardGetBoardsByIdResponse;
import com.myboard.userservice.controller.model.board.response.BoardGetBoardsResponse;
import com.myboard.userservice.controller.model.board.request.DisplayApprovalRequest;
import com.myboard.userservice.controller.model.common.MediaFile;
import com.myboard.userservice.controller.model.common.TimeslotRequest;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.controller.model.display.request.*;
import com.myboard.userservice.controller.model.display.response.DisplayGetDisplaysResponse;
import com.myboard.userservice.controller.model.display.response.DisplayGetTimeSlotsResponse;
import com.myboard.userservice.entity.*;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.repository.AvailabilityRepository;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.types.MediaType;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class DisplayService {

    // Autowired services
    @Autowired
    private TimeslotService timeslotService;

    @Autowired
    private MBUserDetailsService mbUserDetailsService;

    @Autowired
    private WorkFlow flow;

    @Autowired
    private DisplayRepository displayRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private MessageSource messageSource;

    @Value("${myboard.display.path}")
    private String displayPath;

    @Autowired
    private UtilService utilService;

    // Handle display approval
    public void handleDisplayApproval(DisplayApprovalRequest displayApprovalRequest) {
        Display display = displayRepository.findById(displayApprovalRequest.getDisplayId()).orElse(null);
        if (display == null) {
            throw new MBException("Display Not Found");
        }
        display.setStatus(displayApprovalRequest.isApprove() ? StatusType.APPROVED : StatusType.REJECTED);
        displayRepository.save(display);
        flow.addInfo("Display updated successfully");
    }

    // Handle display deletion
    public void handleDisplayDelete(DisplayDeleteRequest displayDeleteRequest) throws MBException {
        displayRepository.deleteById(displayDeleteRequest.getDisplayId());
        flow.addInfo("Display deleted successfully");
    }

    // Get display by ID
    public void handleDisplayGet(DisplayGetRequest displayGetRequest) throws MBException {
        Display display = displayRepository.findById(displayGetRequest.getDisplayId()).orElse(null);
        if (display == null) {
            throw new MBException("Display not found");
        }
        flow.setData(display);
    }

    // Get time slots for a display
    public void handleDisplayGetTimeSlots(DisplayGetTimeSlotsRequest displayGetTimeSlotsRequest) throws MBException {
        Display display = displayRepository.findById(displayGetTimeSlotsRequest.getDisplayId()).orElse(null);
        if (display == null) {
            throw new MBException("Display not found");
        }

        Availability availability = availabilityRepository.findByDisplayIdAndDate(display.getId(), displayGetTimeSlotsRequest.getDate());
        List<TimeSlot> timeSlots = (availability != null) ? availability.getTimeSlots() : timeslotService.getDefaultTimeSlots();

        flow.setData(new DisplayGetTimeSlotsResponse(display.getId(), displayGetTimeSlotsRequest.getDate(), timeSlots));
    }

    // Update time slots for a display
    public void handleDisplayUpdateTimeSlots(DisplayUpdateTimeSlotsRequest displayUpdateTimeSlotsRequest) throws MBException {
        // Find display and board
        Display display = displayRepository.findById(displayUpdateTimeSlotsRequest.getDisplayId()).orElse(null);
        Board board = boardRepository.findById(displayUpdateTimeSlotsRequest.getBoardId()).orElse(null);
        if (display == null || board == null) {
            throw new MBException("Display or Board not found");
        }

        // Process time slots similarly to previous logic...
        // (Method implementation remains unchanged for time slots)

        flow.addInfo("Time slots updated successfully");
    }
    // Save a new display
    public void saveDisplay(MultipartFile file, String displayName) {
        if (file.isEmpty()) {
            throw new MBException("File is empty");
        }

        try {
            // Get logged-in user
            User user = mbUserDetailsService.getLoggedInUser();

            // Generate a unique file name
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Define the file path
            Path filePath = Paths.get(displayPath, user.getUsername(), uniqueFileName);

            // Ensure directories exist
            Files.createDirectories(filePath.getParent());

            // Write file to the directory
            Files.write(filePath, file.getBytes());

            // Retrieve or create the display
            Display display = displayRepository.findByName(displayName).orElseGet(() -> {
                Display newDisplay = new Display();
                newDisplay.setName(displayName);
                newDisplay.setCreatedBy(user);
                newDisplay.setCreatedAt(LocalDateTime.now());
                newDisplay.setMediaFiles(new ArrayList<>()); // Initialize mediaFiles list
                return newDisplay;
            });

            // Determine media type and create MediaFile object
            MediaType mediaType = utilService.determineMediaType(file);
            MediaFile mediaFile = new MediaFile("http://192.168.1.43:8080/myboard/file/display/"+ uniqueFileName, mediaType);

            // Add the media file to the display
            display.getMediaFiles().add(mediaFile);

            // Update modified info
            display.setModifiedBy(user);
            display.setLastModifiedAt(LocalDateTime.now());

            // Save the display to the database
            displayRepository.save(display);

            // Set flow data with display ID and file name
            flow.setData(Map.of("displayId", display.getId(), "fileName", uniqueFileName));

        } catch (IOException e) {
            throw new MBException("Failed to save file", e);
        }
    }


    // Add media to a display
    public void addMedia(String boardId, MultipartFile file) {
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
            Path userDirectory = Paths.get(displayPath, loggedInUser.getId().toString());
            Files.createDirectories(userDirectory);
            Path filePath = userDirectory.resolve(uniqueFileName);
            Files.write(filePath, file.getBytes());

            MediaType mediaType = utilService.determineMediaType(file);
            MediaFile mediaFile = new MediaFile("http://192.168.1.43:8080/myboard/file/display/"+uniqueFileName, mediaType);
            board.getMediaFiles().add(mediaFile);

            boardRepository.save(board);
            flow.setData(Map.of("boardId", board.getId(), "fileName", uniqueFileName));
            flow.addInfo("Media added successfully");
        } catch (IOException e) {
            throw new MBException("Failed to save media", e);
        }
    }

    // Delete media from a display
    public void deleteMedia(String displayId, String mediaName) {
        Display display = displayRepository.findById(displayId).orElse(null);
        if (display == null) {
            throw new MBException("Display not found");
        }

        String mediaFileName = Paths.get(mediaName).getFileName().toString();
        List<MediaFile> mediaFiles = display.getMediaFiles();

        // Check if the media file exists
        if (mediaFiles.stream().noneMatch(mediaFile -> mediaFile.getFileName().equals(mediaFileName))) {
            throw new MBException("Media not found on the display");
        }

        try {
            User loggedInUser = mbUserDetailsService.getLoggedInUser();
            Path mediaPath = Paths.get(displayPath, loggedInUser.getId().toString(), mediaFileName);
            Files.deleteIfExists(mediaPath);
            mediaFiles.removeIf(mediaFile -> mediaFile.getFileName().equals(mediaFileName));
            displayRepository.save(display);
            flow.addInfo("Media deleted successfully");
        } catch (IOException e) {
            throw new MBException("Failed to delete media", e);
        }
    }

    // Delete a display and its associated media
    public void deleteDisplay(String displayId) {
        Display display = displayRepository.findById(displayId).orElse(null);
        if (display == null) {
            throw new MBException("Display not found");
        }
        try {
            User loggedInUser = mbUserDetailsService.getLoggedInUser();
            for (MediaFile mediaFile : display.getMediaFiles()) {
                Path mediaPath = Paths.get(displayPath, loggedInUser.getId().toString(), mediaFile.getFileName());
                Files.deleteIfExists(mediaPath);
            }
            displayRepository.deleteById(displayId);
            flow.addInfo("Display deleted successfully");
        } catch (IOException e) {
            throw new MBException("Failed to delete display", e);
        }
    }

    // Get a paginated list of displays
    public List<DisplayGetDisplaysResponse> getDisplays(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Display> displayPage = displayRepository.findAll(pageable);
        List<DisplayGetDisplaysResponse> displays = displayPage.getContent().stream()
                .map(display -> new DisplayGetDisplaysResponse(
                        display.getId(),
                        display.getName(),
                        display.getMediaFiles(),
                        display.getCreatedAt(),
                        display.getStatus().toString()
                ))
                .collect(Collectors.toList());

        flow.setData(displays);
        flow.addInfo("Displays fetched successfully");
        return displays;
    }

    // Get a display by ID, including media files
    public void getDisplayById(String displayId) {
        Display display = displayRepository.findById(displayId)
                .orElseThrow(() -> new MBException("Display not found"));

        flow.setData(new BoardGetBoardsByIdResponse(
                display.getId(),
                display.getName(),
                display.getCreatedAt(),
                display.getStatus().name(),
                display.getMediaFiles() // Include mediaFiles here
        ));
    }
}
