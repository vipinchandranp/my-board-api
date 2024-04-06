package com.myboard.userservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "replies")
public class BoardCommentReply {

	@Id
	private String id;

	private String text;

	private String userId;

	private String commentId;

	public BoardCommentReply() {
	}

	public BoardCommentReply(String text, String userId, String commentId) {
		this.text = text;
		this.userId = userId;
		this.commentId = commentId;
	}

}
