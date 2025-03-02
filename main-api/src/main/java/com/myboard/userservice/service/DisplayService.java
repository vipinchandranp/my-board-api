package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.board.request.DisplayApprovalRequest;
import com.myboard.userservice.controller.model.common.*;
import com.myboard.userservice.controller.model.display.request.*;
import com.myboard.userservice.controller.model.display.response.CurrentlyPlayingBoardsResponse;
import com.myboard.userservice.controller.model.display.response.DisplayGetDisplaysIdNameLocationResponse;
import com.myboard.userservice.controller.model.display.response.DisplayGetDisplaysResponse;
import com.myboard.userservice.controller.model.display.response.DisplayGetTimeSlotsResponse;
import com.myboard.userservice.entity.*;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.repository.*;
import com.myboard.userservice.types.ItemType;
import com.myboard.userservice.types.MediaType;
import com.myboard.userservice.types.PlayMode;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DisplayService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PinService displayPinService;

    @Value("${myboard.display.searchRadius}")
    private double searchRadius;

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

    @Autowired
    private NSWCheckService nswCheckService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RatingRepository ratingRepository;
    public void addRating(String displayId, double ratingValue) throws MBException {
        // Fetch the display by its ID
        Display display = displayRepository.findById(displayId)
                .orElseThrow(() -> new MBException("Display not found"));

        // Get the logged-in user (assumes you have a service for fetching user details)
        User loggedInUser = mbUserDetailsService.getLoggedInUser();

        // Check if the user has already rated this display
        Rating existingRating = display.getRatings().stream()
                .filter(rating -> rating != null && rating.getRatedBy() != null && rating.getRatedBy().getId().equals(loggedInUser.getId()))
                .findFirst()
                .orElse(null);

        // If the user has already rated, update the existing rating
        if (existingRating != null) {
            // Remove the old rating from the display's ratings list
            display.getRatings().remove(existingRating);

            // Update the existing rating's value and timestamp
            existingRating.setValue(ratingValue);
            existingRating.setTimestamp(System.currentTimeMillis());

            // Save the updated rating to the ratings table (repository)
            ratingRepository.save(existingRating);

            // Add the updated rating back to the display's ratings list
            display.getRatings().add(existingRating);
        } else {
            // If no existing rating, create a new rating
            Rating newRating = Rating.builder()
                    .itemType(ItemType.DISPLAY)    // Set the itemType to DISPLAY
                    .itemID(displayId)             // Set the itemID to the display's ID
                    .ratedBy(loggedInUser)         // Set the ratedBy field to the logged-in user object
                    .value(ratingValue)            // Set the rating value
                    .timestamp(System.currentTimeMillis())  // Set the timestamp of the rating
                    .build();

            // Save the new rating to the ratings table (repository)
            ratingRepository.save(newRating);

            // Add the new rating to the display's ratings list
            display.getRatings().add(newRating);
        }

        // Save the updated display with the new/updated rating
        displayRepository.save(display);

        // Add information to the flow (assuming 'flow' is a logger or some information store)
        flow.addInfo("Rating added successfully to the display");
    }


    public String addComment(String displayId, String commentText) throws MBException {
        Display display = displayRepository.findById(displayId)
                .orElseThrow(() -> new MBException("Display not found"));

        User loggedInUser = mbUserDetailsService.getLoggedInUser();

        Comment comment = Comment.builder()
                .commentedBy(loggedInUser.getId())
                .content(commentText)
                .itemID(displayId)
                .itemType(ItemType.DISPLAY)
                .build();

        commentRepository.save(comment);

        display.getComments().add(comment);
        displayRepository.save(display);
        return comment.getId();
    }

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

    public Page<Display> getFilteredDisplays(AbstractFilterRequest filterRequest, Pageable pageable) {
        User loggedInUser = mbUserDetailsService.getLoggedInUser(); // Get the logged-in user
        filterRequest.setCreatedBy(loggedInUser);

        Page<Display> displayPage = displayRepository.findAllByFilter(filterRequest, Display.class, pageable);

        // Update transient properties for each display
        displayPage.forEach(display -> display.updateUserReaction(loggedInUser));

        return displayPage;
    }

    public void saveDisplay(DisplaySaveRequest displayRequest) {
        if (displayRequest.getFiles().isEmpty()) {
            throw new MBException("Files are empty");
        }

        try {
            // Get logged-in user
            User user = mbUserDetailsService.getLoggedInUser();

// Retrieve or create the display
            Display display = displayRepository.findByName(displayRequest.getDisplayName()).orElseGet(() -> {
                Display newDisplay = new Display();
                newDisplay.setName(displayRequest.getDisplayName());
                newDisplay.setPrice(displayRequest.getPrice());  // Ensure the price is set

                // Set latitude and longitude in the location array
                newDisplay.setLatitude(displayRequest.getLatitude());
                newDisplay.setLongitude(displayRequest.getLongitude());

                // Alternatively, you can set both at once:
                // newDisplay.setLocation(displayRequest.getLatitude(), displayRequest.getLongitude());

                newDisplay.setCreatedBy(user);
                newDisplay.setCreatedTime(LocalDateTime.now());
                newDisplay.setMediaFiles(new ArrayList<>()); // Initialize mediaFiles list
                return newDisplay;
            });


            // Process each file in the request
            for (MultipartFile file : displayRequest.getFiles()) {
                if (file.isEmpty()) {
                    throw new MBException("One of the files is empty");
                }

                // Generate a unique file name
                String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                // Define the file path
                Path filePath = Paths.get(displayPath, user.getUsername(), uniqueFileName);

                // Ensure directories exist
                Files.createDirectories(filePath.getParent());

                // Write file to the directory
                Files.write(filePath, file.getBytes());

                // Determine media type and create MediaFile object
                MediaType mediaType = utilService.determineMediaType(file);
                MediaFile mediaFile = new MediaFile("http://192.168.1.43:8080/myboard/file/display/" + uniqueFileName, mediaType);

                // Add the media file to the display
                display.getMediaFiles().add(mediaFile);
            }

            // Update modified info
            display.setModifiedBy(user);
            display.setLastModifiedTime(LocalDateTime.now());

            // Save the display to the database
            displayRepository.save(display);

            // Generate salt and hashed PIN for the display
            String hashedPin = displayPinService.generateUniqueDisplayPin(display.getId()); // Generate hashed pin

            // Set the hashed pin and salt in the display
            display.setDisplayPin(hashedPin);

            // Save the display to the database after generating the pin
            displayRepository.save(display);

            // Set flow data with display ID and file names
            List<String> fileNames = displayRequest.getFiles().stream()
                    .map(file -> UUID.randomUUID().toString() + "_" + file.getOriginalFilename())
                    .collect(Collectors.toList());
            flow.setData("Display saved successfully !");

        } catch (IOException e) {
            throw new MBException("Failed to save files or generate hashed pin", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hashed pin or salt", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // Add media to a display
    public void addMedia(String displayId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new MBException("File is empty");
        }
        Display display = displayRepository.findById(displayId).orElse(null);
        if (display == null) {
            throw new MBException("Display not found");
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
            display.getMediaFiles().add(mediaFile);

            displayRepository.save(display);
            flow.setData(Map.of("displayId", display.getId(), "fileName", uniqueFileName));
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

    public void deleteDisplay(String displayId) {
        Display display = displayRepository.findById(displayId).orElse(null);
        if (display == null) {
            throw new MBException("Display not found");
        }
        try {
            User loggedInUser = mbUserDetailsService.getLoggedInUser();
            for (MediaFile mediaFile : display.getMediaFiles()) {
                // Extract file name from the URL if necessary
                String fileName = mediaFile.getFileName();
                if (fileName.contains("/")) {
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                }

                // Construct the path using the extracted file name
                Path mediaPath = Paths.get(displayPath, loggedInUser.getId().toString(), fileName);
                Files.deleteIfExists(mediaPath);
            }
            displayRepository.deleteById(displayId);
            flow.addInfo("Display deleted successfully");
        } catch (IOException e) {
            throw new MBException("Failed to delete display", e);
        }
    }

    public List<DisplayGetDisplaysResponse> getDisplays(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Display> displayPage = displayRepository.findAll(pageable);

        List<DisplayGetDisplaysResponse> displays = displayPage.getContent().stream().map(display -> {
            // Extract board IDs from the associated boards
            List<String> boardIds = display.getBoards().stream()
                    .map(Board::getId)
                    .collect(Collectors.toList());

            return DisplayGetDisplaysResponse.builder()
                    .displayId(display.getId())
                    .displayName(display.getName())
                    .mediaFiles(display.getMediaFiles())
                    .createdDateAndTime(display.getCreatedTime())
                    .status(display.getStatus().toString())
                    .latitude(display.getLocation() != null ? display.getLocation()[0] : 0.0)
                    .longitude(display.getLocation() != null ? display.getLocation()[1] : 0.0)
                    .boardIds(boardIds)
                    .displayPin(display.getDisplayPin())
                    .price(display.getPrice())
                    .build();
        }).collect(Collectors.toList());

        flow.setData(displays);
        flow.addInfo("Displays fetched successfully");
        return displays;
    }

    // Get a display by ID, including media files and associated board IDs
    public void getDisplayById(String displayId) {
        Display display = displayRepository.findById(displayId)
                .orElseThrow(() -> new MBException("Display not found"));

        // Extract board IDs from the associated boards
        List<String> boardIds = display.getBoards().stream()
                .map(Board::getId)
                .collect(Collectors.toList());

        DisplayGetDisplaysResponse response = DisplayGetDisplaysResponse.builder()
                .displayId(display.getId())
                .displayName(display.getName())
                .mediaFiles(display.getMediaFiles())
                .createdDateAndTime(display.getCreatedTime())
                .status(display.getStatus().name())
                .latitude(display.getLocation() != null ? display.getLocation()[0] : 0.0)
                .longitude(display.getLocation() != null ? display.getLocation()[1] : 0.0)
                .boardIds(boardIds)
                .displayPin(display.getDisplayPin())
                .price(display.getPrice())
                .build();

        flow.setData(response);
    }



    public void geoTag(DisplayGeoTagRequest geoTagRequest) throws MBException {
        // Validate the display
        Display display = displayRepository.findById(geoTagRequest.getDisplayId()).orElse(null);
        if (display == null) {
            throw new MBException("Display not found");
        }
        // Update the display location
        display.setLatitude(geoTagRequest.getLatitude());
        display.setLongitude(geoTagRequest.getLongitude());
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

    public List<DisplayGetDisplaysIdNameLocationResponse> getNearbyDisplays() throws MBException {
        // Get the logged-in user's location
        User loggedInUser = mbUserDetailsService.getLoggedInUser();
        double[] userLocation = loggedInUser.getLocation();
        if (userLocation == null || userLocation.length != 2) {
            throw new MBException("User location not set");
        }
        List<Display> nearbyDisplays = displayRepository.findNearbyDisplays(userLocation[0], userLocation[1], 600000);

        // Convert the nearby displays to the new response format
        List<DisplayGetDisplaysIdNameLocationResponse> response = nearbyDisplays.stream()
                .map(display -> new DisplayGetDisplaysIdNameLocationResponse(
                        display.getId(),
                        display.getName(),
                        display.getLocation()[0], // Latitude
                        display.getLocation()[1]  // Longitude
                ))
                .collect(Collectors.toList());

        // Add the data to the workflow
        flow.setData(response);
        flow.addInfo("Nearby displays fetched successfully");
        return response;
    }

    // Method to check if a display is within the search radius using Haversine formula
    private boolean isWithinRadius(double[] userLocation, double[] displayLocation, double radiusInKm) {
        double earthRadius = 6371.0; // Radius of the earth in kilometers

        double latDiff = Math.toRadians(displayLocation[0] - userLocation[0]);
        double lonDiff = Math.toRadians(displayLocation[1] - userLocation[1]);

        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(userLocation[0])) * Math.cos(Math.toRadians(displayLocation[0])) *
                        Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = earthRadius * c; // Distance in kilometers

        return distance <= radiusInKm;
    }


    public List<DisplayGetDisplaysIdNameLocationResponse> getAllDisplays() throws MBException {
        // Get the logged-in user's location
        User loggedInUser = mbUserDetailsService.getLoggedInUser();
        double[] userLocation = loggedInUser.getLocation();
        if (userLocation == null || userLocation.length != 2) {
            throw new MBException("User location not set");
        }

        // Get all displays from the repository
        List<Display> allDisplays = displayRepository.findAll();

        // Convert the nearby displays to the new response format
        List<DisplayGetDisplaysIdNameLocationResponse> response = allDisplays.stream()
                .map(display -> new DisplayGetDisplaysIdNameLocationResponse(
                        display.getId(),
                        display.getName(),
                        display.getLocation()[0], // Latitude
                        display.getLocation()[1]  // Longitude
                ))
                .collect(Collectors.toList());

        // Add the data to the workflow
        flow.setData(response);
        flow.addInfo("Nearby displays fetched successfully");
        return response;
    }

    public CurrentlyPlayingBoardsResponse getBoardStatus(String displayId) throws MBException {
        Display display = displayRepository.findById(displayId)
                .orElseThrow(() -> new MBException("Display not found"));

        LocalDateTime now = LocalDateTime.now();

        List<Timeslot> timeslots = timeslotRepository.findByDisplayId(displayId);

        // Filter for currently playing boards with PlayMode.PLAYING
        List<String> currentlyPlaying = timeslots.stream()
                .filter(ts -> ts.getPlayMode() == PlayMode.PLAYING)
                .filter(ts -> ts.getStartTime().isBefore(now) && ts.getEndTime().isAfter(now))
                .map(ts -> ts.getBoard().getId())
                .distinct()
                .collect(Collectors.toList());

        // Filter for previously played boards with PlayMode.PLAYING
        List<String> previouslyPlayed = timeslots.stream()
                .filter(ts -> ts.getPlayMode() == PlayMode.PLAYED)
                .filter(ts -> ts.getEndTime().isBefore(now))
                .map(ts -> ts.getBoard().getId())
                .distinct()
                .collect(Collectors.toList());

        // Filter for upcoming boards with PlayMode.PLAYING
        List<String> upcoming = timeslots.stream()
                .filter(ts -> ts.getPlayMode() == PlayMode.WAITING_TO_PLAY)
                .filter(ts -> ts.getStartTime().isAfter(now))
                .map(ts -> ts.getBoard().getId())
                .distinct()
                .collect(Collectors.toList());

        CurrentlyPlayingBoardsResponse response = new CurrentlyPlayingBoardsResponse();
        response.setCurrentlyPlaying(currentlyPlaying);
        response.setPreviouslyPlayed(previouslyPlayed);
        response.setUpcoming(upcoming);

        return response;
    }

    public List<CommentResponse> getDisplayComments(String displayId) throws MBException {
        // Fetch the display from the repository
        Display display = displayRepository.findById(displayId)
                .orElseThrow(() -> new MBException("Display not found"));

        List<Comment> comments = display.getComments()
                .stream()
                .sorted(Comparator.comparing(Comment::getCreatedTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();


        // Transform comments into the response format with profile pictures
        return comments.stream().map(comment -> {
            User user = userRepository.findById(comment.getCommentedBy())
                    .orElseThrow(() -> new MBException("User not found"));
            return new CommentResponse(
                    comment.getId(),
                    comment.getContent(),
                    comment.getCreatedTime(),
                    user.getProfilePicName(),
                    user.getUsername()
            );
        }).collect(Collectors.toList());
    }

    public void likeDisplay(String displayId) throws MBException {
        Display display = displayRepository.findById(displayId).orElseThrow(() -> new MBException("Display not found"));
        User user = mbUserDetailsService.getLoggedInUser();

        display.addLike(user);
        displayRepository.save(display);
    }

    public void dislikeDisplay(String displayId) throws MBException {
        Display display = displayRepository.findById(displayId).orElseThrow(() -> new MBException("Display not found"));
        User user = mbUserDetailsService.getLoggedInUser();

        display.addDislike(user);
        displayRepository.save(display);
    }

    public void undoLikeDisplay(String displayId) throws MBException {
        Display display = displayRepository.findById(displayId).orElseThrow(() -> new MBException("Display not found"));
        User user = mbUserDetailsService.getLoggedInUser();

        display.removeLike(user);
        displayRepository.save(display);
    }

    public void undoDislikeDisplay(String displayId) throws MBException {
        Display display = displayRepository.findById(displayId).orElseThrow(() -> new MBException("Display not found"));
        User user = mbUserDetailsService.getLoggedInUser();

        display.removeDislike(user);
        displayRepository.save(display);
    }

    public Double getRating(String displayId) throws MBException {
        // Fetch the display entity from the database
        Display display = displayRepository.findById(displayId).orElseThrow(() -> new MBException("Display not found"));

        // Return the rating associated with this display
        // Assuming display has a 'getRating' method or a ratings field
        return display.getAverageRating();  // or calculate the average rating if you have multiple ratings per display
    }

    public Integer getNumberOfLikes(String displayId) throws MBException {
        // Fetch the display entity from the database
        Display display = displayRepository.findById(displayId)
                .orElseThrow(() -> new MBException("Display not found"));

        // Assuming display.getLikes() returns a collection of likes
        return display.getNumberOfLikes();
    }

    public Integer getNumberOfDisLikes(String displayId) throws MBException {
        // Fetch the display entity from the database
        Display display = displayRepository.findById(displayId)
                .orElseThrow(() -> new MBException("Display not found"));

        // Assuming display.getLikes() returns a collection of likes
        return display.getNumberOfDislikes();
    }


}
