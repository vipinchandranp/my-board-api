package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.board.request.DisplayApprovalRequest;
import com.myboard.userservice.controller.model.common.MediaFile;
import com.myboard.userservice.controller.model.common.TimeslotRequest;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.controller.model.display.request.*;
import com.myboard.userservice.controller.model.display.response.DisplayGetDisplaysResponse;
import com.myboard.userservice.controller.model.display.response.DisplayGetTimeSlotsResponse;
import com.myboard.userservice.entity.*;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.repository.TimeslotRepository;
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
import java.time.LocalDate;
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
    private MessageSource messageSource;

    @Value("${myboard.display.path}")
    private String displayPath;

    @Autowired
    private UtilService utilService;

    @Autowired
    private TimeslotRepository timeslotRepository;

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
        // Find the display by its ID
        Display display = displayRepository.findById(displayGetTimeSlotsRequest.getDisplayId()).orElse(null);
        if (display == null) {
            throw new MBException("Display not found");
        }

        // Fetch time slots directly from the Timeslot collection for the given display ID and date
        LocalDateTime startOfDay = displayGetTimeSlotsRequest.getDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        // Query time slots for the given display within the specified date range
        List<Timeslot> existingTimeSlots = timeslotRepository.findByDisplayId(display.getId());

        // Get default time slots from the TimeslotService
        List<Timeslot> defaultTimeSlots = timeslotService.getDefaultTimeSlots();

        // Filter out default time slots that are already present in the existing time slots
        List<Timeslot> availableTimeSlots = defaultTimeSlots.stream()
                .filter(defaultSlot -> existingTimeSlots.stream()
                        .noneMatch(existingSlot -> existingSlot.getStartTime().equals(defaultSlot.getStartTime())
                                && existingSlot.getEndTime().equals(defaultSlot.getEndTime())))
                .collect(Collectors.toList());

        // Set the data in the workflow
        flow.setData(new DisplayGetTimeSlotsResponse(display.getId(), displayGetTimeSlotsRequest.getDate(), availableTimeSlots));
    }


    // Update time slots for a display
    public void handleDisplayUpdateTimeSlots(DisplayUpdateTimeSlotsRequest displayUpdateTimeSlotsRequest) throws MBException {
        // Find display
        Display display = displayRepository.findById(displayUpdateTimeSlotsRequest.getDisplayId()).orElseThrow(() -> new MBException("Display not found"));

        // Process the list of board IDs and time slots
        List<String> boardIds = displayUpdateTimeSlotsRequest.getBoardIds();
        List<TimeslotRequest> timeslotRequests = displayUpdateTimeSlotsRequest.getTimeslots();

        if (boardIds == null || boardIds.isEmpty()) {
            throw new MBException("No boards provided");
        }

        if (timeslotRequests == null || timeslotRequests.isEmpty()) {
            throw new MBException("No time slots provided");
        }

        // Iterate over each board ID to fetch and associate
        for (String boardId : boardIds) {
            Board board = boardRepository.findById(boardId).orElseThrow(() -> new MBException("Board with ID " + boardId + " not found"));
            display.getBoards().add(board); // Add the found board to the list
            for (TimeslotRequest timeslot : timeslotRequests) {
                // Here you would typically save the time slot for the board and display
                saveTimeslotForBoard(display, board, timeslot, displayUpdateTimeSlotsRequest.getDate());
            }
        }

        // Save the updated display
        displayRepository.save(display);


        flow.addInfo("Time slots updated successfully");
    }


    private void saveTimeslotForBoard(Display display, Board board, TimeslotRequest timeslotRequest, LocalDate date) {

        // Extract time components from the timeslot request
        String startTimeStr = timeslotRequest.getStartTime(); // e.g., "00:00"
        String endTimeStr = timeslotRequest.getEndTime(); // e.g., "00:59"

        // Combine date with time
        LocalDateTime startTime = LocalDateTime.parse(date + "T" + startTimeStr);
        LocalDateTime endTime = LocalDateTime.parse(date + "T" + endTimeStr);

        // Create a new Timeslot entity
        Timeslot newTimeslot = Timeslot.builder().display(display) // Set the associated display
                .board(board) // Set the associated board
                .startTime(startTime) // Set the converted start time
                .endTime(endTime) // Set the converted end time
                .status(StatusType.WAITING_FOR_APPROVAL) // Set a default status or use as needed
                .build();

        // Save the timeslot using the TimeslotRepository
        try {
            timeslotRepository.save(newTimeslot); // Ensure you have this repository set up
            flow.addInfo("Timeslot saved successfully for board: " + board.getName());
        } catch (Exception e) {
            throw new MBException("Failed to save timeslot for board: " + board.getName(), e);
        }
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
            MediaFile mediaFile = new MediaFile("http://192.168.1.43:8080/myboard/file/display/" + uniqueFileName, mediaType);

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
            MediaFile mediaFile = new MediaFile("http://192.168.1.43:8080/myboard/file/display/" + uniqueFileName, mediaType);
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

        List<DisplayGetDisplaysResponse> displays = displayPage.getContent().stream().map(display -> {
            // Extract board IDs from the associated boards
            List<String> boardIds = display.getBoards().stream().map(Board::getId) // Assuming Board class has a getId() method
                    .collect(Collectors.toList());

            return new DisplayGetDisplaysResponse(display.getId(), display.getName(), display.getMediaFiles(), display.getCreatedAt(), display.getStatus().toString(), display.getLocation() != null ? display.getLocation()[0] : 0.0, // latitude
                    display.getLocation() != null ? display.getLocation()[1] : 0.0, // longitude
                    boardIds // Include the list of board IDs associated with the display
            );
        }).collect(Collectors.toList());

        flow.setData(displays);
        flow.addInfo("Displays fetched successfully");
        return displays;
    }


    // Get a display by ID, including media files and associated board IDs
    public void getDisplayById(String displayId) {
        Display display = displayRepository.findById(displayId).orElseThrow(() -> new MBException("Display not found"));

        // Extract board IDs from the associated boards
        List<String> boardIds = display.getBoards().stream().map(Board::getId) // Assuming Board class has a getId() method
                .collect(Collectors.toList());

        flow.setData(new DisplayGetDisplaysResponse(display.getId(), display.getName(), display.getMediaFiles(), // Include mediaFiles here
                display.getCreatedAt(), display.getStatus().name(), display.getLocation() != null ? display.getLocation()[0] : 0.0, // Latitude
                display.getLocation() != null ? display.getLocation()[1] : 0.0, // Longitude
                boardIds // Include the list of board IDs associated with the display
        ));
    }


    public void geoTag(DisplayGeoTagRequest geoTagRequest) throws MBException {
        // Validate the display
        Display display = displayRepository.findById(geoTagRequest.getDisplayId()).orElse(null);
        if (display == null) {
            throw new MBException("Display not found");
        }
        // Update the display location
        display.setLocation(new double[]{geoTagRequest.getLatitude(), geoTagRequest.getLongitude()});
        displayRepository.save(display);
        flow.addInfo("Geo-tagging successful");
    }

    // DisplayService.java

    // Modify this method in DisplayService.java
    public List<String> getBoardIdsByDisplayId(String displayId) throws MBException {
        // Validate the display
        Display display = displayRepository.findById(displayId).orElse(null);
        if (display == null) {
            throw new MBException("Display not found");
        }

        // Retrieve all timeslots for the display
        List<Timeslot> timeslots = timeslotRepository.findByDisplayId(displayId);

        // Extract the board IDs from the timeslots
        List<String> boardIds = timeslots.stream().map(timeslot -> timeslot.getBoard().getId()).distinct() // Ensure no duplicates
                .collect(Collectors.toList());
        flow.setData(boardIds);
        return boardIds;
    }


}
