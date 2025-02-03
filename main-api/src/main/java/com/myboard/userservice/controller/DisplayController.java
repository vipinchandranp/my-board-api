package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.board.request.BoardGetBoardsRequest;
import com.myboard.userservice.controller.model.board.request.DisplayApprovalRequest;
import com.myboard.userservice.controller.model.board.response.BoardGetBoardsResponse;
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

@RestController
@CrossOrigin
@RequestMapping("/display")
public class DisplayController extends BaseController {

    @Autowired
    private DisplayService displayService;

    @Autowired
    private WorkFlow flow;

    @GetMapping("/delete/{id}")
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
                                            @RequestParam("price") double price,
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

        // Map the Page<Display> to the response model
        List<DisplayGetDisplaysResponse> displayResponses = displaysPage.getContent().stream()
                .map(display -> new DisplayGetDisplaysResponse(
                        display.getId(), // Assuming getId() gives the display ID
                        display.getName(),
                        display.getMediaFiles(),
                        display.getCreatedTime(),
                        display.getStatus().name(), // Convert enum to string
                        display.getLocation() != null && display.getLocation().length == 2 ? display.getLocation()[0] : 0.0, // Latitude
                        display.getLocation() != null && display.getLocation().length == 2 ? display.getLocation()[1] : 0.0, // Longitude
                        display.getBoards().stream().map(board -> board.getId()).collect(Collectors.toList()), // Collect board IDs
                        display.getDisplayPin(),
                        display.getPrice()
                )) // Mapping Display entity to DisplayGetDisplaysResponse
                .collect(Collectors.toList());

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
}
