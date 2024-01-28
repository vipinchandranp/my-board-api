package com.myboard.userservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	public Board saveBoard(Board board, MultipartFile imageFile) {

		User loggedInUser = SecurityUtils.getLoggedInUser();
		board.setUserId(loggedInUser.getId());

		if (imageFile != null) {
			// Process and store the single image file
			String imageFileName = storeImageFile(board.getUser().getUsername(), board.getId(), imageFile);
			// Save the image file name to the Board entity
			storeImageFile(imageFileName, imageFileName, imageFile);
			board.setFileName(imageFileName);
		}

		// Save the Board entity to MongoDB
		return boardRepository.save(board);
	}

	// Method to process and store a single image file
	private String storeImageFile(String userName, String boardId, MultipartFile imageFile) {
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
	public byte[] getImageBytes(String boardId) {
		try {
			// Retrieve the board details from MongoDB
			Board board = boardRepository.findById(boardId).orElse(null);

			if (board != null) {
				// Get the image file name from the board
				String imageFileName = board.getFileName();

				if (imageFileName != null && !imageFileName.isEmpty()) {
					// Create the full file path
					Path filePath = Paths.get(boardImagesPath, board.getUser().getUsername(), imageFileName);

					// Read the file content into a byte array
					return Files.readAllBytes(filePath);
				}
			}

			throw new RuntimeException("Image not found for Board ID: " + boardId);
		} catch (IOException e) {
			// Handle the exception (log, throw, etc.)
			e.printStackTrace();
			throw new RuntimeException("Failed to load image file", e);
		}
	}

}
