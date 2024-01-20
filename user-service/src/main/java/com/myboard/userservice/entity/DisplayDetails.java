package com.myboard.userservice.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

@Document(collection = "display_details")
public class DisplayDetails {

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

	@DBRef
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

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

	public List<DisplayComment> getComments() {
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

	public void setComments(List<DisplayComment> comments) {
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

	// Method to process and store image files
	public void storeImageFiles(List<MultipartFile> imageFiles) {
		// Assuming you want to store the original filenames in the images property
		for (MultipartFile imageFile : imageFiles) {
			String originalFilename = imageFile.getOriginalFilename();
			this.images.add(originalFilename);
		}
	}

	// You might also want to add more setters based on your requirements

}
