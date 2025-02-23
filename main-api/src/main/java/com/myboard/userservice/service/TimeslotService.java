package com.myboard.userservice.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.myboard.userservice.controller.model.timeslot.request.TimeslotStatusRequest;
import com.myboard.userservice.controller.model.timeslot.response.TimeSlotBoardToBePlayed;
import com.myboard.userservice.controller.model.timeslot.response.TimeslotStatusResponse;
import com.myboard.userservice.entity.*;
import com.myboard.userservice.properties.TimeslotProperties;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.repository.PaymentRepository;
import com.myboard.userservice.repository.TimeslotRepository;
import com.myboard.userservice.types.StatusType;
import jakarta.transaction.Transactional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimeslotService {

    @Autowired
    private TimeslotProperties timeslotProperties;

    @Autowired
    private TimeslotRepository timeslotRepository;

    @Autowired
    private DisplayRepository displayRepository;

    @Autowired
    private MBUserDetailsService userDetailsService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PaymentRepository paymentRepository;

    // Assuming you have a way to get the current date
    private LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now().withSecond(0).withNano(0); // Set seconds and nanoseconds to zero
    }

    public List<Timeslot> getDefaultTimeSlots() {
        List<Timeslot> timeSlots = new ArrayList<>();
        int slotDuration = timeslotProperties.getDefaultDuration(); // Duration in minutes

        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(23, 59);
        LocalTime endOfDay = LocalTime.of(23, 59);

        LocalDateTime currentDateTime = getCurrentDateTime(); // Get current date with zeroed seconds

        while (!startTime.isAfter(endOfDay)) {
            // Calculate the end time for the current slot
            LocalTime slotEndTime = startTime.plusMinutes(slotDuration - 1);
            if (slotEndTime.isAfter(endOfDay)) {
                slotEndTime = endOfDay;
            }

            // Create LocalDateTime objects for the time slots
            LocalDateTime startDateTime = currentDateTime.with(startTime); // Combine current date with startTime
            LocalDateTime endDateTime = currentDateTime.with(slotEndTime); // Combine current date with slotEndTime

            // Create and add the time slot
            Timeslot timeSlot = Timeslot.builder()
                    .startTime(startDateTime) // Set LocalDateTime directly
                    .endTime(endDateTime) // Set LocalDateTime directly
                    .status(StatusType.AVAILABLE)
                    .build();

            timeSlots.add(timeSlot);

            // Move startTime to the next slot
            startTime = slotEndTime.plusMinutes(1);

            // Break the loop if the slotEndTime reaches 23:59
            if (slotEndTime.equals(endOfDay)) {
                break;
            }
        }

        return timeSlots;
    }

    public List<TimeslotStatusResponse> getTimeslotsByFilter(TimeslotStatusRequest request) {
        // Ensure date is not null and create LocalDate range
        LocalDate date = LocalDate.from(request.getDate());
        LocalDate startDate = date; // Start of the day
        LocalDate endDate = date.plusDays(1); // End of the day (exclusive)

        // Fetch displays created by the logged-in user
        List<Display> userDisplays = userDetailsService.getDisplaysOfLoggedInUser();

        // Fetch timeslots for the specific date associated with those displays
        List<Timeslot> timeslots = timeslotRepository.findByDisplayIn(userDisplays);

        // Filter timeslots based on the date range
        List<Timeslot> filteredTimeslots = timeslots.stream()
                .filter(timeslot ->
                        !timeslot.getStartTime().toLocalDate().isBefore(startDate) &&
                                !timeslot.getStartTime().toLocalDate().isAfter(endDate)
                )
                .filter(timeslot ->
                        (request.getDisplayId() == null || timeslot.getDisplay().getId().equals(request.getDisplayId())) && // Filter by displayId
                                (request.getDisplayName() == null || timeslot.getDisplay().getName().equalsIgnoreCase(request.getDisplayName())) && // Filter by displayName
                                (request.getBoardId() == null || timeslot.getBoard().getId().equals(request.getBoardId())) && // Filter by boardId
                                (request.getBoardName() == null || timeslot.getBoard().getName().equalsIgnoreCase(request.getBoardName())) && // Filter by boardName
                                (request.getStatus() == null || timeslot.getStatus().equals(request.getStatus())) // Filter by status
                )
                .collect(Collectors.toList());

        // Convert filtered timeslots to response DTOs
        return filteredTimeslots.stream()
                .map(this::convertToTimeslotStatusResponse)
                .collect(Collectors.toList());
    }


    public List<TimeslotStatusResponse> getTimeslotsByDisplayCreator() {
        User user = userDetailsService.getLoggedInUser();

        // Step 1: Find all displays created by the user
        List<Display> displays = displayRepository.findByCreatedBy(user);

        // Step 2: Find all timeslots associated with those displays
        List<Timeslot> timeslots = timeslotRepository.findByDisplayIn(displays); // Use the correct method name

        // Step 3: Convert Timeslot entities to TimeslotStatusResponse DTOs
        return timeslots.stream()
                .map(this::convertToTimeslotStatusResponse)
                .collect(Collectors.toList());
    }

    // Helper method to convert a Timeslot entity to TimeslotStatusResponse DTO
    private TimeslotStatusResponse convertToTimeslotStatusResponse(Timeslot timeslot) {
        return TimeslotStatusResponse.builder()
                .date(timeslot.getStartTime())         // Assuming 'date' refers to the startTime
                .timeslotId(timeslot.getId())
                .boardId(timeslot.getBoard().getId())  // Get boardId
                .boardName(timeslot.getBoard().getName())
                .displayId(timeslot.getDisplay().getId()) // Get displayId
                .displayName(timeslot.getDisplay().getName())
                .status(timeslot.getStatus().name())   // Convert status enum to string
                .build();
    }

    public boolean updateTimeslotApprovalStatus(String timeslotId, boolean isApproved) {
        try {
            // Fetch the timeslot by its ID
            Timeslot timeslot = timeslotRepository.findById(timeslotId).orElse(null);

            if (timeslot == null) {
                // Timeslot not found
                return false;
            }

            // Update the status based on approval
            if (isApproved) {
                timeslot.setStatus(StatusType.APPROVED); // Assuming APPROVED is a valid status
            } else {
                timeslot.setStatus(StatusType.REJECTED); // Assuming REJECTED is a valid status
            }

            // Save the updated timeslot
            timeslotRepository.save(timeslot);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<LocalDate> getAvailableDatesForUserDisplays() {
        User user = userDetailsService.getLoggedInUser();

        // Step 1: Find all displays created by the user
        List<Display> displays = displayRepository.findByCreatedBy(user);

        // Step 2: Find all timeslots associated with those displays
        List<Timeslot> timeslots = timeslotRepository.findByDisplayIn(displays);

        // Step 3: Extract unique dates from the start time of the timeslots
        List<LocalDate> availableDates = timeslots.stream()
                .map(timeslot -> timeslot.getStartTime().toLocalDate()) // Get LocalDate from startTime
                .distinct() // Get unique dates
                .collect(Collectors.toList());

        return availableDates;
    }
    public TimeSlotBoardToBePlayed getBoardToBePlayedForDisplay(String displayPin) {
        // Fetch the Display entity using the displayPin and created by the logged-in user
        User user = userDetailsService.getLoggedInUser();
        Optional<Display> displayOpt = displayRepository.findByDisplayPinAndCreatedBy(displayPin, user);

        if (displayOpt.isEmpty()) {
            return null; // Display not found for the given pin
        }

        Display display = displayOpt.get();

        Query query = new Query();
        Instant currentTime = Instant.now();

        // Build your query with criteria
        query.addCriteria(Criteria.where("display.$id").is(new ObjectId(display.getId())))
                .addCriteria(Criteria.where("startTime").lte(Date.from(currentTime)))
                .addCriteria(Criteria.where("endTime").gte(Date.from(currentTime)))
                .addCriteria(Criteria.where("status").is(StatusType.APPROVED))
                .addCriteria(Criteria.where("paymentStatus").is(StatusType.PAYMENT_COMPLETED));

        // Execute the query
        Timeslot timeslot = mongoTemplate.findOne(query, Timeslot.class);

        if (timeslot == null) {
            // Generate QR Code as bytes for the display ID
            byte[] qrCodeBytes = generateQRCode(display.getId());
            if (qrCodeBytes != null) {
                return TimeSlotBoardToBePlayed.builder()
                        .displayId(display.getId())
                        .displayName(display.getName())
                        .displayQrCode(qrCodeBytes) // Set the generated QR code bytes
                        .message("Scan QR code")
                        .build();
            } else {
                return null; // QR code generation failed
            }
        }

        // Assuming the board is already set for the timeslot, fetch the board details
        Board board = timeslot.getBoard();
        String boardMediaPath = (board.getMediaFiles() != null && !board.getMediaFiles().isEmpty())
                ? board.getMediaFiles().get(0).getFileName()
                : null;

        // Return the required response with the board and timeslot details
        return TimeSlotBoardToBePlayed.builder()
                .boardId(board.getId())
                .displayId(display.getId())
                .boardName(board.getName())
                .displayName(display.getName())
                .boardMediaPath(boardMediaPath)
                .timeslotId(timeslot.getId())
                .build();
    }

    public byte[] generateQRCode(String displayId) {
        String qrCodeText = displayId;
        int size = 250;

        try {
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);

            // Convert BitMatrix to BufferedImage
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(byteMatrix);

            // Write BufferedImage to ByteArrayOutputStream as PNG
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "PNG", outputStream);

            return outputStream.toByteArray(); // Return QR code as bytes
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Timeslot updatePaymentDetails(String timeslotId, String paymentId) {
        // Fetch the timeslot by ID
        Optional<Timeslot> timeslotOptional = timeslotRepository.findById(timeslotId);
        if (!timeslotOptional.isPresent()) {
            throw new IllegalArgumentException("Timeslot not found with ID: " + timeslotId);
        }

        Timeslot timeslot = timeslotOptional.get();

        // Fetch the payment by ID
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        if (!paymentOptional.isPresent()) {
            throw new IllegalArgumentException("Payment not found with ID: " + paymentId);
        }

        Payment payment = paymentOptional.get();

        // Update the payment details for the timeslot
        timeslot.setPayment(payment);

        // Save the updated timeslot
        return timeslotRepository.save(timeslot);
    }

    public boolean isPaymentCompleted(String timeslotId) {
        // Fetch the timeslot by ID
        Optional<Timeslot> timeslotOptional = timeslotRepository.findById(timeslotId);
        if (!timeslotOptional.isPresent()) {
            throw new IllegalArgumentException("Timeslot not found with ID: " + timeslotId);
        }

        Timeslot timeslot = timeslotOptional.get();

        // Check if the timeslot has an associated payment
        Payment payment = timeslot.getPayment();
        if (payment == null) {
            return false; // No payment associated
        }

        // Return true if the payment status is COMPLETED
        return payment.getStatus() == StatusType.PAYMENT_COMPLETED;
    }

    public Timeslot saveTimeslot(Timeslot timeslot) {
        if (timeslot == null) {
            throw new IllegalArgumentException("Timeslot cannot be null");
        }

        // Ensure that the start time is before the end time
        if (timeslot.getStartTime() == null || timeslot.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }
        if (!timeslot.getStartTime().isBefore(timeslot.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        // Validate the display ID
        if (timeslot.getDisplay() == null || timeslot.getDisplay().getId() == null) {
            throw new IllegalArgumentException("Timeslot must be associated with a valid display");
        }

        // Convert LocalDateTime to Instant
        Instant startTimeInstant = timeslot.getStartTime().toInstant(ZoneOffset.UTC);
        Instant endTimeInstant = timeslot.getEndTime().toInstant(ZoneOffset.UTC);

        // Check if the timeslot overlaps with an existing timeslot for the same display
        List<Timeslot> overlappingTimeslots = timeslotRepository.findByDisplayAndTimeRange(
                timeslot.getDisplay().getId(),
                startTimeInstant,
                endTimeInstant
        );

        if (!overlappingTimeslots.isEmpty()) {
            throw new IllegalArgumentException("Timeslot overlaps with an existing timeslot");
        }

        // Save the new timeslot to the database
        return timeslotRepository.save(timeslot);
    }



}
