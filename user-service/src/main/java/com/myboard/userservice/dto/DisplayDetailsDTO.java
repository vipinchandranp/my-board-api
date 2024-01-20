package com.myboard.userservice.dto;

import java.util.List;

public class DisplayDetailsDTO {

	private String id;
	private double latitude;
	private double longitude;
	private String description;
	private String name;
	private String fileName;
	private String userName;

	private List<DisplayCommentDTO> comments;

	private double rating;
	private String userId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private List<String> images; // Updated to store image paths as strings

	// Getters
	public String getId() {
		return id;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getDescription() {
		return description;
	}

	public List<DisplayCommentDTO> getComments() {
		return comments;
	}

	public double getRating() {
		return rating;
	}

	public String getUserId() {
		return userId;
	}

	public List<String> getImages() {
		return images;
	}

	// Setters
	public void setId(String id) {
		this.id = id;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setComments(List<DisplayCommentDTO> comments) {
		this.comments = comments;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
}
