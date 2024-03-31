package com.myboard.userservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.myboard.userservice.dto.ApprovalIncomingRequestDTO;
import com.myboard.userservice.dto.BoardDTO;
import com.myboard.userservice.dto.BoardWithImageDTO;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.DisplayTimeSlot;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.repository.DisplayTimeSlotRepository;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    private final DisplayTimeSlotRepository displayTimeSlotRepository;

    private final BoardRepository boardRepository;

    @Value("${myboard.board.path}")
    private String boardImagesPath;


    @Autowired
    public ApprovalServiceImpl(DisplayTimeSlotRepository displayTimeSlotRepository, BoardRepository boardRepository) {
        this.displayTimeSlotRepository = displayTimeSlotRepository;
        this.boardRepository = boardRepository;
    }

    @Override
    public List<ApprovalIncomingRequestDTO> getAllApprovalIncomingRequestsForUser(User user) {
        // Retrieve all display time slots for the user
        return displayTimeSlotRepository.findByDisplayOwnerUser(user).stream()
                .map(displayTimeSlot -> {
                    ApprovalIncomingRequestDTO dto = new ApprovalIncomingRequestDTO();
                    dto.setId(displayTimeSlot.getId());
                    dto.setBoardId(displayTimeSlot.getBoard().getId());
                    dto.setDisplayId(displayTimeSlot.getDisplay().getId());
                    dto.setRequesterUserId(displayTimeSlot.getBoardOwnerUser().getId());
                    dto.setBoardTitle(displayTimeSlot.getBoard().getTitle());
                    dto.setRequestDate(displayTimeSlot.getDate());
                    dto.setStartTime(displayTimeSlot.getStartTime());
                    dto.setEndTime(displayTimeSlot.getEndTime());
                    dto.setRequesterName(displayTimeSlot.getBoardOwnerUser().getUsername());
                    dto.setIsApproved(displayTimeSlot.getApproved());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public BoardWithImageDTO getBoardDetailsWithImage(String timeSlotId) {
        // Fetch the DisplayTimeSlot entity by ID
        DisplayTimeSlot displayTimeSlot = displayTimeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new RuntimeException("DisplayTimeSlot not found for ID: " + timeSlotId));

        // Retrieve the associated board details
        BoardDTO boardDTO = mapDisplayTimeSlotToBoardDTO(displayTimeSlot);

        // Retrieve image bytes for the board
        byte[] imageBytes = getImageBytes(displayTimeSlot.getBoard().getId()); // Implement this method in your
        // BoardService

        // Create a BoardWithImage object combining board details and image bytes
        return new BoardWithImageDTO(boardDTO, imageBytes);
    }

    // Method to map DisplayTimeSlot to BoardDTO
    private BoardDTO mapDisplayTimeSlotToBoardDTO(DisplayTimeSlot displayTimeSlot) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(displayTimeSlot.getBoard().getId());
        boardDTO.setTitle(displayTimeSlot.getBoard().getTitle());
        boardDTO.setDescription(displayTimeSlot.getBoard().getDescription());
        // Map other properties if needed
        return boardDTO;
    }

    public byte[] getImageBytes(String boardId) {
        // Retrieve the board details from MongoDB
        Board board = boardRepository.findById(boardId).orElse(null);

        if (board != null) {
            // Get the image file name from the board
            String imageFileName = board.getFileName();

            if (imageFileName != null && !imageFileName.isEmpty()) {
                // Create the full file path
                Path filePath = Paths.get(boardImagesPath, board.getUserBoardOwner().getUsername(), imageFileName);

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
