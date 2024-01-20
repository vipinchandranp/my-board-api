package com.myboard.userservice.dto;

import java.util.List;

public class DisplayCommentDTO {

	private Long id;

	private String text;
	private String username;

	private java.util.Date date;

	private List<BoardCommentReplyDTO> replies;

	// Constructors, getters, and setters

	// Constructors
	public DisplayCommentDTO() {
		// Default constructor required by JPA
	}

	public DisplayCommentDTO(String text, String username, java.util.Date date, List<BoardCommentReplyDTO> replies) {
		this.text = text;
		this.username = username;
		this.date = date;
		this.replies = replies;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public String getUsername() {
		return username;
	}

	public java.util.Date getDate() {
		return date;
	}

	public List<BoardCommentReplyDTO> getReplies() {
		return replies;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public void setReplies(List<BoardCommentReplyDTO> replies) {
		this.replies = replies;
	}
}
