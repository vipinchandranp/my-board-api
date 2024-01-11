package com.myboard.userservice.service;

import com.myboard.userservice.entity.Board;

public interface BoardService {

	Board saveBoard(Board board);

	Board getBoardDetailsById(String boardId);

	byte[] getImageBytes(String imageFileId); // Add this method to retrieve image bytes

}
