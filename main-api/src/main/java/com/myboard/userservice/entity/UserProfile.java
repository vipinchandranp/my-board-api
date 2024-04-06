package com.myboard.userservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "user-profile")
public class UserProfile {

	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String address;
	private String profilePicName;

	@DBRef
	private User user;

}
