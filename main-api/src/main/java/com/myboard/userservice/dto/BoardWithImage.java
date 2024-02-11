package com.myboard.userservice.dto;

import java.io.Serializable;

public class BoardWithImage implements Serializable {
	private static final long serialVersionUID = 1L;

	private final BoardDTO board;
	private final byte[] imageBytes;

	public BoardWithImage(BoardDTO board, byte[] imageBytes) {
		this.board = board;
		this.imageBytes = imageBytes;
	}

	public BoardDTO getBoard() {
		return board;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}
}
