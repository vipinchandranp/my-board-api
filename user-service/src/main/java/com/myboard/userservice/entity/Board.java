package com.myboard.userservice.entity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.client.gridfs.model.GridFSFile;
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
	private String userId; // Reference to the user's ID

	@NotNull
	private List<BoardComment> boardComments;

	@NotNull
	private List<DateTimeSlot> displayDetails;

	@Field("org.springframework.data.mongodb.core.mapping.DBRef")
	private String imageFileId; // Reference to the GridFS file

	// Getter and setter for id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	// Getter and setter for title
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// Getter and setter for description
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// Getter and setter for userId
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	// Getter and setter for boardComments
	public List<BoardComment> getBoardComments() {
		return boardComments;
	}

	public void setBoardComments(List<BoardComment> boardComments) {
		this.boardComments = boardComments;
	}

	// Getter and setter for displayDetails
	public List<DateTimeSlot> getDisplayDetails() {
		return displayDetails;
	}

	public void setDisplayDetails(List<DateTimeSlot> displayDetails) {
		this.displayDetails = displayDetails;
	}

	// Getter and setter for imageFileId
	public String getImageFileId() {
		return imageFileId;
	}

	public void setImageFileId(String imageFileId) {
		this.imageFileId = imageFileId;
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

	// Other getters and setters...

	public void setImageFile(GridFsTemplate gridFsTemplate, InputStream inputStream, String fileName)
			throws IOException {
		// Store the file in GridFS and get the file ID
		imageFileId = gridFsTemplate.store(inputStream, fileName).toString();
	}

	public InputStream getImageFile(GridFsTemplate gridFsTemplate) throws IOException {
	    // Ensure that the imageFileId is not null or empty
	    if (imageFileId == null || imageFileId.isEmpty()) {
	        return null;
	    }

	    // Retrieve the file from GridFS using the file ID
	    GridFSFile gridFsFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(imageFileId)));
	    if (gridFsFile != null) {
	        return gridFsTemplate.getResource(gridFsFile).getInputStream();
	    } else {
	        return null; // Or handle the case where the file is not found
	    }
	}


}
