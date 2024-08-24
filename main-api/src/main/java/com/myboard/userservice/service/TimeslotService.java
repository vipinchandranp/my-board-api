package com.myboard.userservice.service;

import com.myboard.userservice.entity.TimeSlot;
import com.myboard.userservice.properties.TimeslotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeslotService {

    @Autowired
    private TimeslotProperties timeslotProperties;

    public List<TimeSlot> getDefaultTimeSlots() {
        List<TimeSlot> timeSlots = new ArrayList<>();
        int slotDuration = timeslotProperties.getDefaultDuration(); // Duration in minutes

        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(23, 59);
        LocalTime endOfDay = LocalTime.of(23, 59);

        while (!startTime.isAfter(endOfDay)) {
            // Calculate the end time for the current slot
            LocalTime slotEndTime = startTime.plusMinutes(slotDuration - 1);
            if (slotEndTime.isAfter(endOfDay)) {
                slotEndTime = endOfDay;
            }

            // Create and add the time slot
            TimeSlot timeSlot = TimeSlot.builder()
                    .startTime(startTime.toString())
                    .endTime(slotEndTime.toString())
                    .status("available")
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
