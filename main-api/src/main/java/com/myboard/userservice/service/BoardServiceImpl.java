package com.myboard.userservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.myboard.userservice.dto.BoardDTO;
import com.myboard.userservice.dto.BoardWithImage;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.security.SecurityUtils;

@Service
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;

	@Value("${myboard.board.path}")
	private String boardImagesPath;

	@Autowired
	public BoardServiceImpl(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	@Override
	public Board saveBoard(String boardTitle, String boardDesc, MultipartFile imageFile) {
		User loggedInUser = SecurityUtils.getLoggedInUser();

		// Create a new Board entity with the provided title and description
		Board board = new Board();
		board.setTitle(boardTitle);
		board.setDescription(boardDesc);
		board.setUserId(loggedInUser.getId());
		board.setUser(loggedInUser);
		// Save the Board entity to MongoDB and retrieve the saved entity with ID
		Board savedBoard = boardRepository.save(board);

		// Save the image file using the board ID obtained from the savedBoard
		if (imageFile != null) {
			String imageFileName = storeImageFile(savedBoard.getId(), loggedInUser.getUsername(), imageFile);
			savedBoard.setFileName(imageFileName);

			// Update the Board entity in the database with the image file name
			savedBoard = boardRepository.save(savedBoard);
		}

		// Return the saved Board entity, which now contains the generated boardId and
		// image file name
		return savedBoard;
	}

	private String storeImageFile(String boardId, String userName, MultipartFile imageFile) {
		try {
			// Get the original file name from MultipartFile
			String originalFileName = imageFile.getOriginalFilename();

			// Generate a unique file name with timestamp, board ID, and original file
			// extension
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String generatedFileName = userName + "_" + boardId + "_" + UUID.randomUUID().toString() + "_" + timestamp
					+ getFileExtension(originalFileName);

			// Create the full directory path
			Path directory = Paths.get(boardImagesPath, userName);
			Files.createDirectories(directory);

			// Create the full file path
			Path filePath = Paths.get(directory.toString(), generatedFileName);

			// Save the file to the specified directory
			Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			return generatedFileName;
		} catch (IOException e) {
			// Handle the exception (log, throw, etc.)
			e.printStackTrace();
			throw new RuntimeException("Failed to store image file", e);
		}
	}

	// Helper method to extract file extension from a file name
	private String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
	}

	@Override
	public Board getBoardDetailsById(String boardId) {
		// Implement the logic to retrieve board details from the database or other
		// sources
		// You can use Spring Data JPA or any other data access method here
		// For simplicity, let's assume BoardRepository is a repository for board
		// entities

		// BoardRepository.findById is just a placeholder, update it based on your
		// actual data access method
		// You may also need to handle cases where the board details are not found
		// For simplicity, this example assumes that findById returns an optional Board
		return boardRepository.findById(boardId).orElse(null);
	}

	@Override
	public Page<BoardWithImage> getBoardItemsForUser(String userId, Pageable pageable) {
		// Retrieve the paged board items from the repository
		Page<Board> boardPage = boardRepository.findByUserId(userId, pageable);

		// Map each Board to BoardWithImage, including image bytes
		List<BoardWithImage> boardsWithImage = boardPage.getContent().stream().map(this::mapBoardToBoardWithImage)
				.collect(Collectors.toList());

		// Create a new Page with the mapped content and original pageable information
		return new PageImpl<>(boardsWithImage, pageable, boardPage.getTotalElements());
	}

	private BoardWithImage mapBoardToBoardWithImage(Board board) {
		// Map the Board entity to a simplified BoardDTO
		BoardDTO boardDTO = mapBoardToBoardDTO(board);

		// Retrieve image bytes for the current board
		byte[] imageBytes = getImageBytes(board.getId());

		// Create a new BoardWithImage instance with the simplified BoardDTO and image
		// bytes
		return new BoardWithImage(boardDTO, imageBytes);
	}

	private BoardDTO mapBoardToBoardDTO(Board board) {
		// Create a new BoardDTO and set its fields based on the Board entity
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setId(board.getId());
		boardDTO.setTitle(board.getTitle());
		boardDTO.setDescription(board.getDescription());
		// Set other necessary fields

		return boardDTO;
	}

	@Override
	public byte[] getImageBytes(String boardId) {
		// Retrieve the board details from MongoDB
		Board board = boardRepository.findById(boardId).orElse(null);

		if (board != null) {
			// Get the image file name from the board
			String imageFileName = board.getFileName();

			if (imageFileName != null && !imageFileName.isEmpty()) {
				// Create the full file path
				Path filePath = Paths.get(boardImagesPath, board.getUser().getUsername(), imageFileName);

				// Read the file content into a byte array
				try {
					return Files.readAllBytes(filePath);
				} catch (IOException e) {
					// Handle the exception (log, throw, etc.)
					e.printStackTrace();
					throw new RuntimeException("Failed to load image file", e);
				}
			}
		}

		throw new RuntimeException("Image not found for Board ID: " + boardId);
	}
}