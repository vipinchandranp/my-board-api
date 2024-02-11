package com.myboard.userservice.entity;
import java.util.List;

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

    // Getters and setters
    // ...

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(List<String> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }

    public List<String> getNotAvailableTimeSlots() {
        return notAvailableTimeSlots;
    }

    public void setNotAvailableTimeSlots(List<String> notAvailableTimeSlots) {
        this.notAvailableTimeSlots = notAvailableTimeSlots;
    }
}
