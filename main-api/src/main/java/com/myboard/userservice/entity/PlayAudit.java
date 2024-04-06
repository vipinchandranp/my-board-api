package com.myboard.userservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "play_audit")
public class PlayAudit extends SuperEntity {

	@Id
	private String id;

	@DBRef
	private DisplayTimeSlot displayTimeSlot;

	public PlayAudit() {
	}

}
