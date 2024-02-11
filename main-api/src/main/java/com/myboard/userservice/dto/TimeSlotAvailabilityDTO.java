package com.myboard.userservice.dto;
import java.time.LocalDate;
import java.util.List;

public class TimeSlotAvailabilityDTO {
    private LocalDate selectedDate;
    private List<String> availableTimeSlots;
    private List<String> unavailableTimeSlots;

    public TimeSlotAvailabilityDTO(LocalDate selectedDate, List<String> availableTimeSlots, List<String> unavailableTimeSlots) {
        this.selectedDate = selectedDate;
        this.availableTimeSlots = availableTimeSlots;
        this.unavailableTimeSlots = unavailableTimeSlots;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public List<String> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public List<String> getUnavailableTimeSlots() {
        return unavailableTimeSlots;
    }
}
