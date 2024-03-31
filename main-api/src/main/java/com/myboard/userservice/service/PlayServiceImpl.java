package com.myboard.userservice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.Display;
import com.myboard.userservice.entity.DisplayTimeSlot;
import com.myboard.userservice.entity.PlayAudit;
import com.myboard.userservice.entity.TimeSlotAvailability;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.repository.DisplayTimeSlotRepository;
import com.myboard.userservice.repository.PlayAuditRepository;
import com.myboard.userservice.repository.UserRepository;
import com.myboard.userservice.security.SecurityUtils;

import net.glxn.qrgen.QRCode;

@Service
public class PlayServiceImpl implements PlayService {

	@Autowired
	private DisplayRepository displayRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DisplayTimeSlotRepository displayTimeSlotRepository;

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private PlayAuditRepository playAuditRepository;

	@Value("${myboard.board.path}")
	private String boardImagesPath;

	@Override
	public byte[] getDisplayImageForCurrentTime() {
		try {
			// Get the current date and time
			LocalDate currentDate = LocalDate.now();
			LocalTime currentTime = LocalTime.now();

			User loggedInUser = SecurityUtils.getLoggedInUser();
			List<DisplayTimeSlot> timeSlots = displayTimeSlotRepository.findByDisplayOwnerUser(loggedInUser);

			Display display = null;
			// Iterate through the retrieved time slots
			for (DisplayTimeSlot timeSlot : timeSlots) {
				display = timeSlot.getDisplay();
				List<String> timeslot = display.getDateToTimeSlots()
						.getOrDefault(currentDate, new TimeSlotAvailability()).getBookedTimeSlots();

				// Check if the current time falls within any booked time slot for the display
				for (String timeSlotStr : timeslot) {
					LocalTime startTime = LocalTime.parse(timeSlotStr.split("-")[0].trim());
					LocalTime endTime = LocalTime.parse(timeSlotStr.split("-")[1].trim());

					if (timeSlot.getApproved() && currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
						// If the current time is within a booked time slot, retrieve the corresponding
						// board image
						// Retrieve the image bytes for the board
						String boardId = timeSlot.getBoard().getId();
						savePlayAudit(timeSlot.getId());
						return getImageBytes(boardId);
					}
				}
			}

			// If the current time is not within any booked time slot for any display,
			// generate and return the QR code image
			ByteArrayOutputStream qrOutputStream = new ByteArrayOutputStream();
			QRCode.from(display.getId()).writeTo(qrOutputStream);

			// Set the appropriate content type
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_PNG);

			return qrOutputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to fetch display image for current time: " + e.getMessage());
		}
	}

	public byte[] getImageBytes(String boardId) {
		// Retrieve the board details from MongoDB or any other data source
		Board board = boardRepository.findById(boardId).orElse(null);

		if (board != null) {
			// Get the image file name from the board
			String imageFileName = board.getFileName();

			if (imageFileName != null && !imageFileName.isEmpty()) {
				// Create the full file path
				Path filePath = Paths.get(boardImagesPath, board.getUserBoardOwner().getUsername(), imageFileName);

				try {
					// Read the file content into a byte array
					return Files.readAllBytes(filePath);
				} catch (IOException e) {
					// Handle the exception (log, throw, etc.)
					e.printStackTrace();
					throw new RuntimeException("Failed to load image file", e);
				}
			}
		}

		// If the board or its image is not found, throw an exception or return null
		throw new RuntimeException("Image not found for Board ID: " + boardId);
	}

	@Override
	public void savePlayAudit(String displayTimeSlotId) {
		// Retrieve DisplayTimeSlot object from repository
		DisplayTimeSlot displayTimeSlot = displayTimeSlotRepository.findById(displayTimeSlotId)
				.orElseThrow(() -> new NoSuchElementException("DisplayTimeSlot not found"));

		// Create a new PlayAudit object
		PlayAudit playAudit = new PlayAudit();

		// Assign the retrieved DisplayTimeSlot object to the PlayAudit object
		playAudit.setDisplayTimeSlot(displayTimeSlot);

		// Save the PlayAudit object
		playAuditRepository.save(playAudit);
	}

}
