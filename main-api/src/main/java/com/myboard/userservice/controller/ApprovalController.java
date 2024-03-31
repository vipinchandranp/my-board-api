package com.myboard.userservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.myboard.userservice.dto.ApprovalOutgoingRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myboard.userservice.dto.ApprovalIncomingRequestDTO;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.security.SecurityUtils;
import com.myboard.userservice.service.DisplayTimeSlotService;

@RestController
@RequestMapping("/v1/approval")
@CrossOrigin
public class ApprovalController {

    private final DisplayTimeSlotService displayTimeSlotService;

    @Autowired
    public ApprovalController(DisplayTimeSlotService displayTimeSlotService) {
        this.displayTimeSlotService = displayTimeSlotService;
    }

    @GetMapping("/incoming/display-time-slots")
    public ResponseEntity<List<ApprovalIncomingRequestDTO>> getIncomingApprovalList() {
        // Get the logged-in user
        User loggedInUser = SecurityUtils.getLoggedInUser();

        // Retrieve all display time slots for the logged-in user
        List<ApprovalIncomingRequestDTO> incomingRequests = displayTimeSlotService
                .getAllDisplayTimeSlotsForUser(loggedInUser).stream()
                .map(displayTimeSlot -> {
                    ApprovalIncomingRequestDTO dto = new ApprovalIncomingRequestDTO();
                    dto.setId(displayTimeSlot.getId());
                    dto.setBoardId(displayTimeSlot.getBoard().getId());
                    dto.setDisplayId(displayTimeSlot.getDisplay().getId());
                    dto.setRequesterUserId(displayTimeSlot.getBoardOwnerUser().getId());
                    dto.setBoardTitle(displayTimeSlot.getBoard().getTitle());
                    dto.setDisplayTitle(displayTimeSlot.getDisplay().getName());
                    dto.setRequestDate(displayTimeSlot.getDate());
                    dto.setStartTime(displayTimeSlot.getStartTime());
                    dto.setEndTime(displayTimeSlot.getEndTime());
                    dto.setRequesterName(displayTimeSlot.getBoardOwnerUser().getUsername());
                    dto.setIsApproved(displayTimeSlot.getApproved());
                    return dto;
                })
                .collect(Collectors.toList());

        // Check if any incoming requests were found
        if (!incomingRequests.isEmpty()) {
            return ResponseEntity.ok(incomingRequests);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/outgoing/display-time-slots")
    public ResponseEntity<List<ApprovalOutgoingRequestDTO>> getOutgoingApprovalList() {
        // Get the logged-in user
        User loggedInUser = SecurityUtils.getLoggedInUser();

        // Retrieve all display time slots for the logged-in user
        List<ApprovalOutgoingRequestDTO> incomingRequests = displayTimeSlotService
                .getAllDisplayTimeSlotsForUser(loggedInUser).stream()
                .map(displayTimeSlot -> {
                    ApprovalOutgoingRequestDTO dto = new ApprovalOutgoingRequestDTO();
                    dto.setId(displayTimeSlot.getId());
                    dto.setBoardId(displayTimeSlot.getBoard().getId());
                    dto.setDisplayId(displayTimeSlot.getDisplay().getId());
                    dto.setDisplayOwnerUserId(displayTimeSlot.getDisplayOwnerUser().getId());
                    dto.setDisplayOwnerUserName(displayTimeSlot.getDisplayOwnerUser().getUsername());
                    dto.setRequesterUserId(displayTimeSlot.getBoardOwnerUser().getId());
                    dto.setBoardTitle(displayTimeSlot.getBoard().getTitle());
                    dto.setDisplayTitle(displayTimeSlot.getDisplay().getName());
                    dto.setRequestDate(displayTimeSlot.getDate());
                    dto.setStartTime(displayTimeSlot.getStartTime());
                    dto.setEndTime(displayTimeSlot.getEndTime());
                    dto.setRequesterName(displayTimeSlot.getBoardOwnerUser().getUsername());
                    dto.setIsApproved(displayTimeSlot.getApproved());
                    return dto;
                })
                .collect(Collectors.toList());

        // Check if any incoming requests were found
        if (!incomingRequests.isEmpty()) {
            return ResponseEntity.ok(incomingRequests);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/approve-display-time-slot/approve/{timeSlotId}")
    public ResponseEntity<String> approveTimeSlot(@PathVariable String timeSlotId) {
        // Update the approval status of the time slot with the provided ID
        boolean success = displayTimeSlotService.approveTimeSlot(timeSlotId);
        if (success) {
            return ResponseEntity.ok("Time slot approved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to approve time slot");
        }
    }

    @PutMapping("/approve-display-time-slot/reject/{timeSlotId}")
    public ResponseEntity<String> rejectTimeSlot(@PathVariable String timeSlotId) {
        // Update the approval status of the time slot with the provided ID
        boolean success = displayTimeSlotService.approveTimeSlot(timeSlotId);
        if (success) {
            return ResponseEntity.ok("Time slot approved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to approve time slot");
        }
    }
}
