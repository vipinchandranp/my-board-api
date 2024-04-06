package com.myboard.userservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "play")
public class Play extends SuperEntity {

	@Id
	private String id;

	private Boolean isPlaying;

	@DBRef
	private DisplayTimeSlot displayTimeSlot;

}
