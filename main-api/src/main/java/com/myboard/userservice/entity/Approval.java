package com.myboard.userservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "boards")
public class Approval {

	@Id
	private String id;

}
