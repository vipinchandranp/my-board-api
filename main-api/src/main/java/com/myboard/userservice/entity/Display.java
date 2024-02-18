package com.myboard.userservice.entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "display")
public class Display {

	@Id
	private String id;
	private double latitude;
	private double longitude;
	private String description;
	private String name;
	private String fileName;

	@DBRef
	private List<DisplayComment> comments;

	private double rating;
	private String userId;

	private List<String> images;

	@DBRef
	private User userDisplayOwner;

	private Map<LocalDate, TimeSlotAvailability> dateToTimeSlots = new HashMap<>();

	public Map<LocalDate, TimeSlotAvailability> getDateToTimeSlots() {
		return dateToTimeSlots;
	}

	public void setDateToTimeSlots(Map<LocalDate, TimeSlotAvailability> dateToTimeSlots) {
		this.dateToTimeSlots = dateToTimeSlots;
	}

	public void addTimeSlotsForDate(LocalDate date, TimeSlotAvailability timeSlotAvailability) {
		this.dateToTimeSlots.put(date, timeSlotAvailability);
	}

	private String timePeriodUnit;

	@Override
	public String toString() {
		return "DisplayDetails{" + "id='" + id + '\'' + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", description='" + description + '\'' + ", name='" + name + '\'' + ", fileName='" + fileName + '\''
				+ ", comments=" + comments + ", rating=" + rating + ", userId='" + userId + '\'' + ", images=" + images
				+ ", userDisplayOwner=" + userDisplayOwner + ", dateToTimeSlots=" + dateToTimeSlots
				+ ", timePeriodUnit='" + timePeriodUnit + '\'' + '}';
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<DisplayComment> getComments() {
		return comments;
	}

	public void setComments(List<DisplayComment> comments) {
		this.comments = comments;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public User getUserDisplayOwner() {
		return userDisplayOwner;
	}

	public void setUserDisplayOwner(User userDisplayOwner) {
		this.userDisplayOwner = userDisplayOwner;
	}

	public String getTimePeriodUnit() {
		return timePeriodUnit;
	}

	public void setTimePeriodUnit(String timePeriodUnit) {
		this.timePeriodUnit = timePeriodUnit;
	}

}
