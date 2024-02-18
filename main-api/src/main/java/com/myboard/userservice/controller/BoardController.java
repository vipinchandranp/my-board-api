package com.myboard.userservice.controller;

import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myboard.userservice.dto.BoardWithImage;
import com.myboard.userservice.dto.DisplayDateTimeSlotDTO;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.security.SecurityUtils;
import com.myboard.userservice.service.BoardService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/v1/board")
@CrossOrigin
public class BoardController {
	private final BoardService boardService;
	private final BoardRepository boardRepository;

	@Autowired
	public BoardController(GridFsTemplate gridFsTemplate, BoardService boardService, BoardRepository boardRepository) {
		this.boardService = boardService;
		this.boardRepository = boardRepository;
	}

	@PostMapping(value = "/save", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<String> createBoard(@RequestParam("boardTitle") String boardTitle,
			@RequestParam("boardDesc") String boardDesc, @RequestParam("displayDetails") String displayDetailsJson,
			@RequestPart("imageFile") MultipartFile imageFile) {
		// Save the board using the boardService
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			DisplayDateTimeSlotDTO displayDateTimeSlotDTO = objectMapper.readValue(displayDetailsJson,
					DisplayDateTimeSlotDTO.class);

			Board savedBoard = boardService.saveBoard(boardTitle, boardDesc, displayDateTimeSlotDTO, imageFile);

			if (savedBoard != null) {
				return ResponseEntity.status(HttpStatus.CREATED).body("Board created successfully");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create board");
			}
		} catch (Exception e) {
			// Handle the exception (e.g., log it or return an error response)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process image file");
		}
	}

	@GetMapping("/title-id")
	public List<BoardRepository.ProjectionTitleIdDto> getBoardItemsTitle() {
		User loggedInUser = SecurityUtils.getLoggedInUser();
		return boardRepository.getTitleAndId(loggedInUser.getId());
	}

	@GetMapping("/details/{boardId}")
	public void getBoardDetailsById(@PathVariable String boardId, HttpServletResponse response) {
		try {
			// Call the boardService to get details based on boardId
			Board boardDetails = boardService.getBoardDetailsById(boardId);

			// Fetch image bytes
			byte[] imageBytes = boardService.getImageBytes(boardId);

			// Set content type and length
			response.setContentType(MediaType.IMAGE_PNG_VALUE); // Adjust content type based on your image type
			response.setContentLength(imageBytes.length);

			// Stream the image content
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(imageBytes);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			// Handle exceptions, log the error, and return an INTERNAL_SERVER_ERROR status
			e.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	@GetMapping("/items")
	public ResponseEntity<List<BoardWithImage>> getBoardItemsForUser(@RequestParam int page, @RequestParam int size) {
		try {
			User loggedInUser = SecurityUtils.getLoggedInUser();
			Page<BoardWithImage> boardPage = boardService.getBoardItemsForUser(loggedInUser.getId(),
					PageRequest.of(page - 1, size));

			List<BoardWithImage> boardList = boardPage.getContent();
			return ResponseEntity.ok(boardList);
		} catch (Exception e) {
			// Log the exception or handle it based on your requirements
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
