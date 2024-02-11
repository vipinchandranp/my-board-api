package com.myboard.userservice.entity;

import java.sql.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.myboard.userservice.dto.DateTimeSlot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Document(collection = "boards")
public class Board {

	@Id
	private String id;

	@NotBlank
	private String title;

	@NotBlank
	private String description;

	@NotBlank
	private String userId;

	@NotNull
	private List<BoardComment> boardComments;

	@NotNull
	private List<DateTimeSlot> displayDetails;

	private String fileName;

	@DBRef
	private User user;

	@CreatedDate
	private Date createdDate;

	@LastModifiedDate
	private Date modifiedDate;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Board() {
	}

	public Board(@NotBlank String title, @NotBlank String description, @NotBlank String userId,
			@NotNull List<BoardComment> boardComments, @NotNull List<DateTimeSlot> displayDetails) {
		this.title = title;
		this.description = description;
		this.userId = userId;
		this.boardComments = boardComments;
		this.displayDetails = displayDetails;
	}

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

	public List<BoardComment> getBoardComments() {
		return boardComments;
	}

	public void setBoardComments(List<BoardComment> boardComments) {
		this.boardComments = boardComments;
	}

	public List<DateTimeSlot> getDisplayDetails() {
		return displayDetails;
	}

	public void setDisplayDetails(List<DateTimeSlot> displayDetails) {
		this.displayDetails = displayDetails;
	}

}
