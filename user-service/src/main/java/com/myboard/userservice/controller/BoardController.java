package com.myboard.userservice.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	private final GridFsTemplate gridFsTemplate;

	@Autowired
	public BoardController(GridFsTemplate gridFsTemplate, BoardService boardService, BoardRepository boardRepository) {
		this.boardService = boardService;
		this.boardRepository = boardRepository;
		this.gridFsTemplate = gridFsTemplate;
	}

	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> createBoard(@RequestPart("boardTitle") String boardTitle,
			@RequestPart("boardDesc") String boardDesc, @RequestPart("imageFile") MultipartFile imageFile) {
		// Convert the BoardDTO to Board entity
		Board board = new Board();
		board.setTitle(boardTitle);
		board.setDescription(boardDesc);
		User loggedInUser = SecurityUtils.getLoggedInUser();
		board.setUserId(loggedInUser.getId());

		try {
			if (imageFile != null && !imageFile.isEmpty()) {
				// Set the image file in the Board entity using GridFsTemplate
				board.setImageFile(gridFsTemplate, imageFile.getInputStream(), imageFile.getOriginalFilename());
			}

			// Save the board using the boardService
			Board savedBoard = boardService.saveBoard(board);

			if (savedBoard != null) {
				return ResponseEntity.status(HttpStatus.CREATED).body("Board created successfully");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create board");
			}
		} catch (IOException e) {
			// Handle the exception (e.g., log it or return an error response)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process image file");
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateBoard(@PathVariable String id, @RequestBody Board updatedBoard) {
		try {
			Optional<Board> existingBoardOptional = boardRepository.findById(id);
			if (existingBoardOptional.isPresent()) {
				Board existingBoard = existingBoardOptional.get();
				// Update the fields of the existing board object
				existingBoard.setTitle(updatedBoard.getTitle());
				existingBoard.setDescription(updatedBoard.getDescription());
				existingBoard.setDisplayDetails(updatedBoard.getDisplayDetails());
				// Save the updated board in the repository
				boardRepository.save(existingBoard);
				return ResponseEntity.ok("Board updated successfully");
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/items")
	public List<Board> getBoardItemsForUser() {
		User loggedInUser = SecurityUtils.getLoggedInUser();
		return boardRepository.findByUserId(loggedInUser.getId());
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
			byte[] imageBytes = boardService.getImageBytes(boardDetails.getImageFileId());

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

}
