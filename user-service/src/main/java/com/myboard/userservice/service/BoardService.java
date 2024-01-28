package com.myboard.userservice.service;

import org.springframework.web.multipart.MultipartFile;

import com.myboard.userservice.entity.Board;

public interface BoardService {

	Board saveBoard(Board board, MultipartFile imageFile);

	Board getBoardDetailsById(String boardId);

	byte[] getImageBytes(String imageFileId); // Add this method to retrieve image bytes

}
