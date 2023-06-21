package com.myboard.userservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "boards")
public class Board {

	@Id
	private String id;

	private String title;

	private String description;

	private String userId; // Reference to the user's ID

	// Constructors, getters, and setters

	public Board() {
	}

	public Board(String title, String description, String userId) {
		this.title = title;
		this.description = description;
		this.userId = userId;
	}

	// Getters and setters

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
