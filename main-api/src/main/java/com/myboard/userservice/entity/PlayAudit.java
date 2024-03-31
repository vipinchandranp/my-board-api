package com.myboard.userservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "play_audit")
public class PlayAudit extends SuperEntity {

	@Id
	private String id;

	@DBRef
	private DisplayTimeSlot displayTimeSlot;

	public PlayAudit() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DisplayTimeSlot getDisplayTimeSlot() {
		return displayTimeSlot;
	}

	public void setDisplayTimeSlot(DisplayTimeSlot displayTimeSlot) {
		this.displayTimeSlot = displayTimeSlot;
	}

}
