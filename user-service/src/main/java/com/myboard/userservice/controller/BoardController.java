package com.myboard.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myboard.userservice.dto.BoardDTO;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.service.BoardService;

@RestController
@RequestMapping("/v1/board")
public class BoardController {
	private final BoardService boardService;
	private final BoardRepository boardRepository;

	@Autowired
	public BoardController(BoardService boardService, BoardRepository boardRepository) {
		this.boardService = boardService;
		this.boardRepository = boardRepository;
	}

	@PostMapping(("/save"))
	public ResponseEntity<String> createBoard(@RequestBody BoardDTO boardDTO) {
		// Convert the BoardDTO to Board entity
		Board board = new Board();
		board.setUserId(boardDTO.getUserId());
		board.setTitle(boardDTO.getTitle());
		board.setDescription(boardDTO.getDescription());

		// Save the board using the boardService
		Board savedBoard = boardService.saveBoard(board);

		if (savedBoard != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body("Board created successfully");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create board");
		}
	}

	@GetMapping("/items/{userId}")
	public List<Board> getBoardItemsForUser(@PathVariable String userId) {
		return boardRepository.findByUserId(userId);
	}
}