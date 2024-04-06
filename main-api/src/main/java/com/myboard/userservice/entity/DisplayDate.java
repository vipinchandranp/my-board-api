package com.myboard.userservice.entity;
import lombok.Data;

import java.util.List;
@Data
public class DisplayDate {

    private String date; // You may use a specific Date type or String as per your needs

    private List<String> availableTimeSlots;
    private List<String> notAvailableTimeSlots;

    // Constructors, getters, and setters

    public DisplayDate() {
        // Default constructor
    }

    public DisplayDate(String date, List<String> availableTimeSlots, List<String> notAvailableTimeSlots) {
        this.date = date;
        this.availableTimeSlots = availableTimeSlots;
        this.notAvailableTimeSlots = notAvailableTimeSlots;
    }

}
