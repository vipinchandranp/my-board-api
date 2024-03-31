package com.myboard.userservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BoardWithImageDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private final BoardDTO board;
	private final byte[] imageBytes;
}
