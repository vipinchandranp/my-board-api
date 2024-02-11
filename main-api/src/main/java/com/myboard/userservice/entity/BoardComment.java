package com.myboard.userservice.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
public class BoardComment {

	@Id
	private String id;

	private String text;

	private String boardId;

	private String userId;

	private List<BoardCommentReply> replies;

	public BoardComment() {
	}

	public BoardComment(String text, String userId, String boardId, List<BoardCommentReply> replies) {
		this.text = text;
		this.userId = userId;
		this.replies = replies;
		this.boardId = boardId;
	}

	public String getBoardId() {
		return boardId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
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

	public List<BoardCommentReply> getReplies() {
		return replies;
	}

	public void setReplies(List<BoardCommentReply> replies) {
		this.replies = replies;
	}
}
