package com.myboard.userservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.myboard.userservice.dto.BoardWithImage;
import com.myboard.userservice.entity.Board;

public interface BoardService {

	Board getBoardDetailsById(String boardId);

	byte[] getImageBytes(String imageFileId); // Add this method to retrieve image bytes

	Board saveBoard(String boardTitle, String boardDesc, MultipartFile imageFile);

	Page<BoardWithImage> getBoardItemsForUser(String userId, Pageable pageable);

}
