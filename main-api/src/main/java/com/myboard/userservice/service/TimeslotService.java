package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.timeslot.response.TimeslotStatusResponse;
import com.myboard.userservice.entity.Display;
import com.myboard.userservice.entity.Timeslot;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.properties.TimeslotProperties;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.repository.TimeslotRepository;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeslotService {

    @Autowired
    private TimeslotProperties timeslotProperties;

    @Autowired
    private TimeslotRepository timeslotRepository;

    @Autowired
    private DisplayRepository displayRepository;

    @Autowired
    private MBUserDetailsService userDetailsService;


    // Assuming you have a way to get the current date
    private LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now().withSecond(0).withNano(0); // Set seconds and nanoseconds to zero
    }

    public List<Timeslot> getDefaultTimeSlots() {
        List<Timeslot> timeSlots = new ArrayList<>();
        int slotDuration = timeslotProperties.getDefaultDuration(); // Duration in minutes

        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(23, 59);
        LocalTime endOfDay = LocalTime.of(23, 59);

        LocalDateTime currentDateTime = getCurrentDateTime(); // Get current date with zeroed seconds

        while (!startTime.isAfter(endOfDay)) {
            // Calculate the end time for the current slot
            LocalTime slotEndTime = startTime.plusMinutes(slotDuration - 1);
            if (slotEndTime.isAfter(endOfDay)) {
                slotEndTime = endOfDay;
            }

            // Create LocalDateTime objects for the time slots
            LocalDateTime startDateTime = currentDateTime.with(startTime); // Combine current date with startTime
            LocalDateTime endDateTime = currentDateTime.with(slotEndTime); // Combine current date with slotEndTime

            // Create and add the time slot
            Timeslot timeSlot = Timeslot.builder()
                    .startTime(startDateTime) // Set LocalDateTime directly
                    .endTime(endDateTime) // Set LocalDateTime directly
                    .status(StatusType.AVAILABLE)
                    .build();

            timeSlots.add(timeSlot);

            // Move startTime to the next slot
            startTime = slotEndTime.plusMinutes(1);

            // Break the loop if the slotEndTime reaches 23:59
            if (slotEndTime.equals(endOfDay)) {
                break;
            }
        }

        return timeSlots;
    }

    public List<TimeslotStatusResponse> getTimeslotsByDisplayCreator() {
        User user = userDetailsService.getLoggedInUser();

        // Step 1: Find all displays created by the user
        List<Display> displays = displayRepository.findByCreatedBy(user);

        // Step 2: Find all timeslots associated with those displays
        List<Timeslot> timeslots = timeslotRepository.findByDisplayIn(displays); // Use the correct method name

        // Step 3: Convert Timeslot entities to TimeslotStatusResponse DTOs
        return timeslots.stream()
                .map(this::convertToTimeslotStatusResponse)
                .collect(Collectors.toList());
    }

    // Helper method to convert a Timeslot entity to TimeslotStatusResponse DTO
    private TimeslotStatusResponse convertToTimeslotStatusResponse(Timeslot timeslot) {
        return TimeslotStatusResponse.builder()
                .date(timeslot.getStartTime())         // Assuming 'date' refers to the startTime
                .timeslotId(timeslot.getId())
                .boardId(timeslot.getBoard().getId())  // Get boardId
                .boardName(timeslot.getBoard().getName())
                .displayId(timeslot.getDisplay().getId()) // Get displayId
                .displayName(timeslot.getDisplay().getName())
                .status(timeslot.getStatus().name())   // Convert status enum to string
                .build();
    }

    public boolean updateTimeslotApprovalStatus(String timeslotId, boolean isApproved) {
        try {
            // Fetch the timeslot by its ID
            Timeslot timeslot = timeslotRepository.findById(timeslotId).orElse(null);

            if (timeslot == null) {
                // Timeslot not found
                return false;
            }

            // Update the status based on approval
            if (isApproved) {
                timeslot.setStatus(StatusType.APPROVED); // Assuming APPROVED is a valid status
            } else {
                timeslot.setStatus(StatusType.REJECTED); // Assuming REJECTED is a valid status
            }

            // Save the updated timeslot
            timeslotRepository.save(timeslot);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
