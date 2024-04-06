package com.myboard.userservice.entity;

import java.util.List;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
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

}
