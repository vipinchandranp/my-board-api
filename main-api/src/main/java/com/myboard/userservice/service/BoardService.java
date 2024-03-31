package com.myboard.userservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.myboard.userservice.dto.BoardWithImageDTO;
import com.myboard.userservice.dto.DisplayDateTimeSlotDTO;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.User;

public interface BoardService {

	Board getBoardDetailsById(String boardId);

	byte[] getImageBytes(String imageFileId); // Add this method to retrieve image bytes

	Board saveBoard(String boardTitle, String boardDesc, DisplayDateTimeSlotDTO displayDetails,
			MultipartFile imageFile);

	Page<BoardWithImageDTO> getBoardItemsForUser(User userId, Pageable pageable);

}
