package com.myboard.userservice.entity;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "display_time_slots")
public class DisplayTimeSlots {

	@Id
	private String id;
	private String date; // You may use a specific Date type or String as per your needs
	private String userId;

	private Map<String, Boolean> timeSlotsAvailability; // Map<timeSlot, isAvailable>

	// Constructors, getters, and setters

	// ... (same as the previous version)

	@Override
	public String toString() {
		return "DisplayTimeSlots{" + "id='" + id + '\'' + ", date='" + date + '\'' + ", userId='" + userId + '\''
				+ ", timeSlotsAvailability=" + timeSlotsAvailability + '}';
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, Boolean> getTimeSlotsAvailability() {
		return timeSlotsAvailability;
	}

	public void setTimeSlotsAvailability(Map<String, Boolean> timeSlotsAvailability) {
		this.timeSlotsAvailability = timeSlotsAvailability;
	}

}
