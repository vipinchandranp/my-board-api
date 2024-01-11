package com.myboard.userservice.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.bson.types.ObjectId;
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
	private String userId;

	@NotNull
	private List<BoardComment> boardComments;

	@NotNull
	private List<DateTimeSlot> displayDetails;

	@Field("org.springframework.data.mongodb.core.mapping.DBRef")
	private String imageFileId;

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

	public String getImageFileId() {
		return imageFileId;
	}

	public void setImageFileId(String imageFileId) {
		this.imageFileId = imageFileId;
	}

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

		// Convert the String imageFileId to ObjectId
		ObjectId objectId = new ObjectId(imageFileId);

		// Retrieve the file from GridFS using the file ID
		GridFSFile gridFsFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(objectId)));
		if (gridFsFile != null) {
			GridFsResource resource = gridFsTemplate.getResource(gridFsFile);
			return resource.getInputStream();
		} else {
			return null; // Or handle the case where the file is not found
		}
	}

	private byte[] imageBytes; // Add this line

	// ... other class members ...

	// Add this method
	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	// Add this method if you need to get the image bytes
	public byte[] getImageBytes() {
		return imageBytes;
	}

}
