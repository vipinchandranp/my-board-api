package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.board.request.BoardGetBoardsRequest;
import com.myboard.userservice.controller.model.board.request.DisplayApprovalRequest;
import com.myboard.userservice.controller.model.board.response.BoardGetBoardsResponse;
import com.myboard.userservice.controller.model.common.CommentRequest;
import com.myboard.userservice.controller.model.common.CommentResponse;
import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.controller.model.display.request.*;
import com.myboard.userservice.controller.model.display.response.CurrentlyPlayingBoardsResponse;
import com.myboard.userservice.controller.model.display.response.DisplayGetBoardIdsResponse;
import com.myboard.userservice.controller.model.display.response.DisplayGetDisplaysResponse;
import com.myboard.userservice.controller.model.display.response.DisplayGetDisplaysIdNameLocationResponse;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.Display;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.service.DisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@CrossOrigin
@RequestMapping("/display")
public class DisplayController extends BaseController {

    @Autowired
    private DisplayService displayService;

    @Autowired
    private WorkFlow flow;

    @GetMapping("/delete/{displayId}")
    public MainResponse delete(@PathVariable String displayId) throws MBException, IOException {
        DisplayDeleteRequest displayDeleteRequest = new DisplayDeleteRequest();
        displayDeleteRequest.setDisplayId(displayId);
        displayService.handleDisplayDelete(displayDeleteRequest);
        return new MainResponse<>(flow);
    }

    @GetMapping("/get/{displayId}")
    public MainResponse get(@PathVariable String displayId) throws MBException, IOException {
        DisplayGetRequest displayGetRequest = new DisplayGetRequest();
        displayGetRequest.setDisplayId(displayId);
        displayService.handleDisplayGet(displayGetRequest);
        return new MainResponse<>(flow);
    }

    @GetMapping("/get/time-slots")
    public MainResponse getTimeSlots(@RequestParam String displayId, @RequestParam String date) throws MBException, IOException {
        // Parse the date string into a LocalDate object
        LocalDate localDate = LocalDate.parse(date);

        // Create request object
        DisplayGetTimeSlotsRequest displayGetTimeSlotRequest = new DisplayGetTimeSlotsRequest();
        displayGetTimeSlotRequest.setDisplayId(displayId);
        displayGetTimeSlotRequest.setDate(localDate);

        // Handle the request
        displayService.handleDisplayGetTimeSlots(displayGetTimeSlotRequest);

        return new MainResponse<>(flow);
    }


    @PutMapping("/update/time-slots")
    public MainResponse updateTimeSlots(@RequestBody DisplayUpdateTimeSlotsRequest displayUpdateTimeSlotsRequest) throws MBException, IOException {
        displayService.handleDisplayUpdateTimeSlots(displayUpdateTimeSlotsRequest);
        return new MainResponse<>(flow);
    }


    @PutMapping("/approval")
    public MainResponse approval(@RequestBody DisplayApprovalRequest displayApprovalRequest) throws MBException {
        displayService.handleDisplayApproval(displayApprovalRequest);
        return new MainResponse<>(flow);
    }

    @PostMapping("/save")
    public MainResponse<String> saveDisplay(@RequestParam("displayName") String displayName,
                                            @RequestParam(value = "price", required = false) Double price,
                                            @RequestParam(value = "latitude", required = false) Double latitude,
                                            @RequestParam(value = "longitude", required = false) Double longitude,
                                            @RequestParam("files") List<MultipartFile> files) throws IOException {
        // Map request data to SaveDisplay model
        DisplaySaveRequest saveDisplay = new DisplaySaveRequest();
        saveDisplay.setDisplayName(displayName);
        saveDisplay.setPrice(price);
        saveDisplay.setLatitude(latitude);
        saveDisplay.setLongitude(longitude);
        saveDisplay.setFiles(files);

        // Save the display using the service layer
        displayService.saveDisplay(saveDisplay);

        return buildResponse();
    }

    @PutMapping("/media/add/{displayId}")
    public MainResponse<String> addMedia(@PathVariable String displayId, @RequestParam("file") MultipartFile file) throws MBException, IOException {
        displayService.addMedia(displayId, file);
        return buildResponse();
    }

    @PutMapping("/geo-tag")
    public MainResponse<String> geoTag(@RequestBody DisplayGeoTagRequest geoTagRequest) throws MBException, IOException {
        displayService.geoTag(geoTagRequest);
        return buildResponse();
    }


    @DeleteMapping("/media/delete/{displayId}/{mediaName}")
    public MainResponse<String> deleteMedia(@PathVariable String displayId, @PathVariable String mediaName) throws MBException {
        displayService.deleteMedia(displayId, mediaName);
        return buildResponse();
    }

    @DeleteMapping("/delete/{displayId}")
    public MainResponse<String> deleteDisplay(@PathVariable String displayId) throws MBException {
        displayService.deleteDisplay(displayId);
        return buildResponse();
    }

    @GetMapping("/list")
    public MainResponse<List<DisplayGetDisplaysResponse>> getDisplays(DisplayGetDisplaysRequest request) throws MBException {
        // Get the paginated and filtered list of displays from the service
        Page<Display> displaysPage = displayService.getFilteredDisplays(request, PageRequest.of(request.getPage(), request.getSize()));

        // Map the Page<Display> to the response model using the builder
        List<DisplayGetDisplaysResponse> displayResponses = displaysPage.getContent().stream()
                .map(display -> DisplayGetDisplaysResponse.builder()
                        .displayId(display.getId()) // Assuming getId() gives the display ID
                        .displayName(display.getName())
                        .mediaFiles(display.getMediaFiles())
                        .createdDateAndTime(display.getCreatedTime())
                        .status(display.getStatus().name()) // Convert enum to string
                        .latitude(display.getLocation() != null && display.getLocation().length == 2 ? display.getLocation()[0] : 0.0) // Latitude
                        .longitude(display.getLocation() != null && display.getLocation().length == 2 ? display.getLocation()[1] : 0.0) // Longitude
                        .boardIds(display.getBoards().stream().map(board -> board.getId()).collect(toList())) // Collect board IDs
                        .displayPin(display.getDisplayPin())
                        .price(display.getPrice())
                        .likedByCurrentUser(display.isLikedByCurrentUser())
                        .dislikedByCurrentUser(display.isDislikedByCurrentUser())
                        .numberOfLikes(display.getNumberOfLikes())
                        .numberOfDislikes(display.getNumberOfDislikes())
                        .build())
                .collect(toList());

        // Build and return the response with pagination data
        return buildResponse(displayResponses, displaysPage.getTotalElements(), displaysPage.getTotalPages(), displaysPage.getNumber());
    }



    @GetMapping("/{displayId}")
    public MainResponse<DisplayGetDisplaysResponse> getDisplayById(@PathVariable String displayId) throws MBException {
        displayService.getDisplayById(displayId);
        return buildResponse();
    }

    // Modify this method in DisplayController.java
    @GetMapping("/boards/{displayId}")
    public MainResponse<DisplayGetBoardIdsResponse> getBoardIdsByDisplayId(@PathVariable String displayId) throws MBException {
        displayService.getBoardIdsByDisplayId(displayId);
        return buildResponse();
    }

    @GetMapping("/nearby")
    public MainResponse<List<DisplayGetDisplaysIdNameLocationResponse>> getNearbyDisplays() throws MBException {
        List<DisplayGetDisplaysIdNameLocationResponse> nearbyDisplays = displayService.getNearbyDisplays();
        return new MainResponse<>(nearbyDisplays);
    }

    @GetMapping("/all")
    public MainResponse<List<DisplayGetDisplaysIdNameLocationResponse>> getAllDisplays() throws MBException {
        List<DisplayGetDisplaysIdNameLocationResponse> getAllDisplays = displayService.getAllDisplays();
        return new MainResponse<>(getAllDisplays);
    }

    @GetMapping("/boards/status/{displayId}")
    public MainResponse<CurrentlyPlayingBoardsResponse> getBoardStatusByDisplayId(@PathVariable String displayId) throws MBException {
        CurrentlyPlayingBoardsResponse response = displayService.getBoardStatus(displayId);
        return new MainResponse<>(response);
    }


    // Endpoint to add a rating to a board
    @PostMapping("/{displayId}/rating")
    public MainResponse<String> addRating(@PathVariable String displayId, @RequestParam double rating) throws MBException {
        displayService.addRating(displayId, rating);
        return buildResponse("Rating added successfully");
    }

    // Endpoint to get the rating of a display
    @GetMapping("/{displayId}/rating")
    public MainResponse<Double> getRating(@PathVariable String displayId) throws MBException {
        // Call the service to get the rating of the display
        Double rating = displayService.getRating(displayId);
        // Return the rating wrapped in the MainResponse
        return buildResponse(rating);
    }


    // Modified controller to accept comment text in request body
    @PostMapping("/{displayId}/comment")
    public MainResponse<String> addComment(
            @PathVariable String displayId,
            @RequestBody CommentRequest commentRequest) throws MBException {

        // Access the comment text from the request body
        String commentText = commentRequest.getCommentText();

        // Call service to add the comment (using displayId and commentText)
        String commentId = displayService.addComment(displayId, commentText);

        return buildResponse(commentId);  // Return the response with the comment ID
    }


    @GetMapping("/{displayId}/comments")
    public MainResponse<List<CommentResponse>> getCommentsForDisplay(@PathVariable String displayId) throws MBException {
        // Fetch comments with user profile details from the service
        List<CommentResponse> comments = displayService.getDisplayComments(displayId);
        // Return the response encapsulated in MainResponse
        return new MainResponse<>(comments);
    }

    // Endpoint to like a display
    @PostMapping("/like/{displayId}")
    public MainResponse<String> likeDisplay(@PathVariable String displayId) throws MBException {
        // Call the service layer to handle the "like" action
        displayService.likeDisplay(displayId);
        return buildResponse("Display liked successfully");
    }

    // Endpoint to dislike a display
    @PostMapping("/dislike/{displayId}")
    public MainResponse<String> dislikeDisplay(@PathVariable String displayId) throws MBException {
        // Call the service layer to handle the "dislike" action
        displayService.dislikeDisplay(displayId);
        return buildResponse("Display disliked successfully");
    }
    @PostMapping("/undo-like/{displayId}")
    public MainResponse<String> undoLikeDisplay(@PathVariable String displayId) throws MBException {
        // Call the service layer to handle the "undo like" action
        displayService.undoLikeDisplay(displayId);
        return buildResponse("Like removed successfully");
    }

    // Endpoint to undo a dislike on a display
    @PostMapping("/undo-dislike/{displayId}")
    public MainResponse<String> undoDislikeDisplay(@PathVariable String displayId) throws MBException {
        // Call the service layer to handle the "undo dislike" action
        displayService.undoDislikeDisplay(displayId);
        return buildResponse("Dislike removed successfully");
    }

    @GetMapping("/{displayId}/likes")
    public MainResponse<Integer> getNumberOfLikesForDisplay(@PathVariable String displayId) throws MBException {
        Integer numberOfLikes = displayService.getNumberOfLikes(displayId);
        return buildResponse(numberOfLikes);
    }

    @GetMapping("/{displayId}/dislikes")
    public MainResponse<Integer> getNumberOfDisLikesForBoard(@PathVariable String displayId) throws MBException {
        Integer numberOfDisLikes = displayService.getNumberOfDisLikes(displayId);
        return buildResponse(numberOfDisLikes);
    }


}
