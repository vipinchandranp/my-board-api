package com.myboard.userservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
