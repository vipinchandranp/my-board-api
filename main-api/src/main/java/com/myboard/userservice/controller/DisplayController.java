package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.board.response.BoardGetBoardsResponse;
import com.myboard.userservice.controller.model.board.request.DisplayApprovalRequest;
import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.controller.model.display.request.*;
import com.myboard.userservice.controller.model.display.response.DisplayGetDisplaysResponse;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.service.DisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
    public MainResponse getTimeSlots(@RequestParam String displayId, @RequestParam LocalDate date) throws MBException, IOException {
        DisplayGetTimeSlotsRequest displayGetTimeSlotRequest = new DisplayGetTimeSlotsRequest();
        displayGetTimeSlotRequest.setDisplayId(displayId);
        displayGetTimeSlotRequest.setDate(date);
        displayService.handleDisplayGetTimeSlots(displayGetTimeSlotRequest);
        return new MainResponse<>(flow);
    }

    @PutMapping("/update/time-slots")
    public MainResponse updateTimeSlots(@RequestBody DisplayUpdateTimeSlotsRequest displaySaveTimeSlots) throws MBException, IOException {
        displayService.handleDisplayUpdateTimeSlots(displaySaveTimeSlots);
        return new MainResponse<>(flow);
    }

    @PutMapping("/approval")
    public MainResponse approval(@RequestBody DisplayApprovalRequest displayApprovalRequest) throws MBException {
        displayService.handleDisplayApproval(displayApprovalRequest);
        return new MainResponse<>(flow);
    }

    @PostMapping("/media/save")
    public MainResponse<String> saveDisplay(@RequestParam("file") MultipartFile file, @RequestParam String displayName) throws MBException, IOException {
        displayService.saveDisplay(file, displayName);
        return buildResponse();
    }

    @PutMapping("/media/add/{boardId}")
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
    public MainResponse<String> deleteBoard(@PathVariable String displayId) throws MBException {
        displayService.deleteDisplay(displayId);
        return buildResponse();
    }

    @GetMapping("/list")
    public MainResponse<List<DisplayGetDisplaysResponse>> getBoards(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "4") int size) throws MBException {
        displayService.getDisplays(page, size);
        return buildResponse();
    }

    @GetMapping("/{displayId}")
    public MainResponse<DisplayGetDisplaysResponse> getBoardById(@PathVariable String displayId) throws MBException {
        displayService.getDisplayById(displayId);
        return buildResponse();
    }

}
