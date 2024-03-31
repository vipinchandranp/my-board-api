package com.myboard.userservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "play")
public class Play extends SuperEntity {

	@Id
	private String id;

	private Boolean isPlaying;

	@DBRef
	private DisplayTimeSlot displayTimeSlot;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIsPlaying() {
		return isPlaying;
	}

	public void setIsPlaying(Boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public DisplayTimeSlot getDisplayTimeSlot() {
		return displayTimeSlot;
	}

	public void setDisplayTimeSlot(DisplayTimeSlot displayTimeSlot) {
		this.displayTimeSlot = displayTimeSlot;
	}

}
