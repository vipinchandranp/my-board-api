package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.common.MainRequest;
import com.myboard.userservice.controller.model.common.TimeslotRequest;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.controller.model.display.*;
import com.myboard.userservice.entity.*;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.repository.AvailabilityRepository;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.types.APIType;
import com.myboard.userservice.types.MediaType;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisplayService {

    @Autowired
    private TimeslotService timeslotService;

    @Autowired
    private MBUserDetailsService mbUserDetailsService;

    @Autowired
    private WorkFlow flow;

    @Autowired
    private DisplayRepository displayRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private MessageSource messageSource;

    @Value("${myboard.board.path}")
    private String boardPath;

    public void process(MainRequest baseRequest, APIType apiType) throws MBException {
        try {
            switch (apiType) {
                case DISPLAY_GET:
                    handleDisplayGet((DisplayGetRequest) baseRequest);
                    break;
                case DISPLAY_SAVE:
                    handleDisplaySave((DisplaySaveRequest) baseRequest);
                    break;
                case DISPLAY_UPDATE:
                    handleDisplayUpdate((DisplayUpdateRequest) baseRequest);
                    break;
                case DISPLAY_DELETE:
                    handleDisplayDelete((DisplayDeleteRequest) baseRequest);
                    break;
                case DISPLAY_GET_TIMESLOTS:
                    handleDisplayGetTimeSlots((DisplayGetTimeSlotsRequest) baseRequest);
                    break;
                case DISPLAY_UPDATE_TIMESLOTS:
                    handleDisplayUpdateTimeSlots((DisplayUpdateTimeSlotsRequest) baseRequest);
                    break;
                default:
                    throw new MBException("Invalid API type");
            }
        } catch (Exception e) {
            throw new MBException(e, e.getMessage());
        }
    }

    private void handleDisplaySave(DisplaySaveRequest displaySaveRequest) throws MBException, IOException {
        // Check if the board already exists
        if (displayRepository.existsByName(displaySaveRequest.getName())) {
            flow.addError(messageSource.getMessage("display.save.failure", null, Locale.getDefault()));
            throw new MBException();
        }
        User loggedInUser = mbUserDetailsService.getLoggedInUser();
        MultipartFile mediaContent = displaySaveRequest.getMediaContent();
        Media media = Media.builder()
                .mediaType(MediaType.IMAGE)
                .mediaLocation(boardPath + "/" + loggedInUser)
                .mediaName(Optional.ofNullable(mediaContent).map(MultipartFile::getName).orElse(null))
                .mediaContent(Optional.ofNullable(mediaContent).map(m -> {
                    try {
                        return m.getBytes();
                    } catch (IOException e) {
                        // Handle exception and provide default content
                        return new byte[0];
                    }
                }).orElse(new byte[0]))
                .build();
        Display display = Display.builder()
                .name(displaySaveRequest.getName())
                .location(new double[]{displaySaveRequest.getLongitude(), displaySaveRequest.getLatitude()})
                .media(media)
                .build();
        display.setName(displaySaveRequest.getName());
        displayRepository.save(display);
        // Prepare the response
        String saveMessage = null;
        if (display.getId() != null) {
            saveMessage = messageSource.getMessage("display.save.success", null, Locale.getDefault());
        } else {
            saveMessage = messageSource.getMessage("display.save.failure", null, Locale.getDefault());
        }

        flow.addInfo(saveMessage);
    }

    private void handleDisplayUpdate(DisplayUpdateRequest boardUpdateRequest) throws MBException, IOException {
        Display display = displayRepository.findById(boardUpdateRequest.getDisplayId()).orElse(null);
        if (display == null) {
            String message = messageSource.getMessage("display.update.failure", null, Locale.getDefault());
            throw new MBException(message);
        }
        User loggedInUser = mbUserDetailsService.getLoggedInUser();
        MultipartFile mediaContent = boardUpdateRequest.getMediaContent();
        Media media = Media.builder()
                .mediaType(MediaType.IMAGE)
                .mediaLocation(boardPath + "/" + loggedInUser)
                .mediaName(mediaContent.getName())
                .mediaContent(mediaContent.getBytes())
                .build();
        display.setMedia(media);
        displayRepository.save(display);
        // Prepare the response
        String boardSaveMessage = null;
        if (display.getId() != null) {
            boardSaveMessage = messageSource.getMessage("display.update.success", null, Locale.getDefault());
        } else {
            boardSaveMessage = messageSource.getMessage("display.update.failure", null, Locale.getDefault());
        }
        flow.addInfo(boardSaveMessage);
    }

    private void handleDisplayDelete(DisplayDeleteRequest displayDeleteRequest) throws MBException, IOException {
        try {
            displayRepository.deleteById(displayDeleteRequest.getDisplayId());
        } catch (Exception e) {
            String message = messageSource.getMessage("display.delete.failure", null, Locale.getDefault());
            throw new MBException(message);
        }
        String displaySaveMessage = messageSource.getMessage("display.delete.success", null, Locale.getDefault());
        flow.addInfo(displaySaveMessage);
    }

    private void handleDisplayGet(DisplayGetRequest displayGetRequest) throws MBException, IOException {
        Display display = displayRepository.findById(displayGetRequest.getDisplayId()).orElse(null);
        if (display == null) {
            String message = messageSource.getMessage("display.get.failure", null, Locale.getDefault());
            throw new MBException(message);
        }
        flow.setData(display);
    }

    private void handleDisplayGetTimeSlots(DisplayGetTimeSlotsRequest displayGetTimeSlotsRequest) throws MBException, IOException {
        // Find the display by its ID
        Display display = displayRepository.findById(displayGetTimeSlotsRequest.getDisplayId()).orElse(null);
        if (display == null) {
            String message = messageSource.getMessage("display.get.time-slots.failure", null, Locale.getDefault());
            throw new MBException(message);
        }

        // Fetch the availability for the specific date from the AvailabilityRepository
        Availability availability = availabilityRepository.findByDisplayIdAndDate(display.getId(), displayGetTimeSlotsRequest.getDate());

        List<TimeSlot> timeSlots;

        if (availability == null) {
            // If availability is not found, create default time slots using TimeslotService
            timeSlots = timeslotService.getDefaultTimeSlots();
        } else {
            // Filter and retrieve the time slots for the specific date
            timeSlots = availability.getTimeSlots();
        }

        // Wrap the time slots and date in a response object
        DisplayGetTimeSlotsResponse response = new DisplayGetTimeSlotsResponse(display.getId(), displayGetTimeSlotsRequest.getDate(), timeSlots);
        flow.setData(response);
    }

    private void handleDisplayUpdateTimeSlots(DisplayUpdateTimeSlotsRequest displayUpdateTimeSlotsRequest) throws MBException, IOException {
        // Find the display by its ID
        Display display = displayRepository.findById(displayUpdateTimeSlotsRequest.getDisplayId()).orElse(null);
        if (display == null) {
            String message = messageSource.getMessage("Display not found", null, Locale.getDefault());
            throw new MBException(message);
        }

        Board board = boardRepository.findById(displayUpdateTimeSlotsRequest.getBoardId()).orElse(null);
        if (board == null) {
            String message = messageSource.getMessage("Board not found", null, Locale.getDefault());
            throw new MBException(message);
        }

        // Get default timeslots
        List<TimeSlot> defaultTimeSlots = timeslotService.getDefaultTimeSlots();

        // Check if availability for the given date already exists
        Availability existingAvailability = availabilityRepository.findByDisplayIdAndDate(display.getId(), displayUpdateTimeSlotsRequest.getDate());

        if (existingAvailability != null) {
            // If availability exists, update the existing time slots

            // Create a list of existing timeslots
            List<TimeSlot> updatedTimeSlots = existingAvailability.getTimeSlots();

            // Update the timeslots with the new ones from the request
            for (TimeslotRequest timeslotRequest : displayUpdateTimeSlotsRequest.getTimeslots()) {
                // Find matching timeslot by start and end time
                Optional<TimeSlot> existingSlotOpt = updatedTimeSlots.stream()
                        .filter(ts -> ts.getStartTime().equals(timeslotRequest.getStartTime()) && ts.getEndTime().equals(timeslotRequest.getEndTime()))
                        .findFirst();

                if (existingSlotOpt.isPresent()) {
                    // If the timeslot exists, update its status
                    TimeSlot existingSlot = existingSlotOpt.get();
                    existingSlot.setStatus(StatusType.UNAVAILABLE);
                } else {
                    // If the timeslot does not exist, add it
                    updatedTimeSlots.add(convertToEntity(timeslotRequest, StatusType.AVAILABLE));
                }
            }

            // Combine with default timeslots (add those that are missing)
            for (TimeSlot defaultSlot : defaultTimeSlots) {
                boolean isPresent = updatedTimeSlots.stream()
                        .anyMatch(ts -> ts.getStartTime().equals(defaultSlot.getStartTime()) && ts.getEndTime().equals(defaultSlot.getEndTime()));

                if (!isPresent) {
                    updatedTimeSlots.add(defaultSlot);
                }
            }

            existingAvailability.setTimeSlots(updatedTimeSlots);
            availabilityRepository.save(existingAvailability);
        } else {
            // If availability does not exist, create a new entry
            Availability newAvailability = new Availability();
            newAvailability.setDisplay(display);
            newAvailability.setBoard(board);
            newAvailability.setDate(displayUpdateTimeSlotsRequest.getDate());

            // Convert request timeslots and add them
            List<TimeSlot> newTimeSlots = displayUpdateTimeSlotsRequest.getTimeslots()
                    .stream()
                    .map(request -> convertToEntity(request, StatusType.AVAILABLE))
                    .collect(Collectors.toList());

            // Combine with default timeslots
            for (TimeSlot defaultSlot : defaultTimeSlots) {
                boolean isPresent = newTimeSlots.stream()
                        .anyMatch(ts -> ts.getStartTime().equals(defaultSlot.getStartTime()) && ts.getEndTime().equals(defaultSlot.getEndTime()));

                if (!isPresent) {
                    newTimeSlots.add(defaultSlot);
                }
            }

            newAvailability.setTimeSlots(newTimeSlots);
            availabilityRepository.save(newAvailability);
        }

        // Prepare the success message
        String saveMessage = messageSource.getMessage("display.update.time-slots.success", null, Locale.getDefault());
        flow.addInfo(saveMessage);
    }


    // Helper method to convert TimeslotRequest to TimeSlot entity
    private TimeSlot convertToEntity(TimeslotRequest timeslotRequest, StatusType statusType) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(timeslotRequest.getStartTime());
        timeSlot.setEndTime(timeslotRequest.getEndTime());
        timeSlot.setStatus(statusType);
        return timeSlot;
    }

}
