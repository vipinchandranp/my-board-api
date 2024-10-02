package com.myboard.userservice.service;

import com.myboard.userservice.entity.Timeslot;
import com.myboard.userservice.properties.TimeslotProperties;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeslotService {

    @Autowired
    private TimeslotProperties timeslotProperties;

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
}
