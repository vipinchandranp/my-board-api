package com.myboard.userservice.entity;

import java.sql.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "boards")
public class Board extends SuperEntity{

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

	private String fileName;

	@DBRef
	private User userBoardOwner;

	@CreatedDate
	private Date createdDate;

	@LastModifiedDate
	private Date modifiedDate;

}
