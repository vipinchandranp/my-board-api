package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.*;
import com.myboard.userservice.entity.*;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.repository.AvailabilityRepository;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.types.APIType;
import com.myboard.userservice.types.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class DisplayService {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkFlow flow;

    @Autowired
    private DisplayRepository displayRepository;

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
                case DISPLAY_SAVE_TIMESLOTS:
                    handleDisplaySaveTimeSlots((DisplaySaveTimeSlotsRequest) baseRequest);
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
        User loggedInUser = userService.getLoggedInUser();
        MultipartFile mediaContent = displaySaveRequest.getMediaContent();
        Media media = Media.builder()
                .mediaType(MediaType.IMAGE)
                .mediaLocation(boardPath + "/" + loggedInUser)
                .mediaName(mediaContent.getName())
                .mediaContent(mediaContent.getBytes())
                .build();

        Display display = new Display(media);
        displayRepository.save(display);
        // Prepare the response
        String saveMessage = null;
        if (display.getId() > 0) {
            saveMessage = messageSource.getMessage("display.save.success", null, Locale.getDefault());
        } else {
            saveMessage = messageSource.getMessage("display.save.failure", null, Locale.getDefault());
        }

        flow.addInfo(saveMessage);
    }

    private void handleDisplayUpdate(DisplayUpdateRequest boardUpdateRequest) throws MBException, IOException {
        Display display = displayRepository.findById(boardUpdateRequest.getId()).orElse(null);
        if (display == null) {
            String message = messageSource.getMessage("display.update.failure", null, Locale.getDefault());
            throw new MBException(message);
        }
        User loggedInUser = userService.getLoggedInUser();
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
        if (display.getId() > 0) {
            boardSaveMessage = messageSource.getMessage("display.update.success", null, Locale.getDefault());
        } else {
            boardSaveMessage = messageSource.getMessage("display.update.failure", null, Locale.getDefault());
        }
        flow.addInfo(boardSaveMessage);
    }

    private void handleDisplayDelete(DisplayDeleteRequest displayDeleteRequest) throws MBException, IOException {
        try {
            displayRepository.deleteById(displayDeleteRequest.getId());
        } catch (Exception e) {
            String message = messageSource.getMessage("display.delete.failure", null, Locale.getDefault());
            throw new MBException(message);
        }
        String displaySaveMessage = messageSource.getMessage("display.delete.success", null, Locale.getDefault());
        flow.addInfo(displaySaveMessage);
    }

    private void handleDisplayGet(DisplayGetRequest displayGetRequest) throws MBException, IOException {
        Display display = displayRepository.findById(displayGetRequest.getId()).orElse(null);
        if (display == null) {
            String message = messageSource.getMessage("display.get.failure", null, Locale.getDefault());
            throw new MBException(message);
        }
        flow.setData(display);
    }
    private void handleDisplayGetTimeSlots(DisplayGetTimeSlotsRequest displayGetTimeSlotsRequest) throws MBException, IOException {
        // Find the display by its ID
        Display display = displayRepository.findById(displayGetTimeSlotsRequest.getId()).orElse(null);
        if (display == null) {
            String message = messageSource.getMessage("display.get.time-slots.failure", null, Locale.getDefault());
            throw new MBException(message);
        }

        // Fetch the availability for the specific date from the AvailabilityRepository
        Availability availability = availabilityRepository.findByDisplayIdAndDate(display.getId(), displayGetTimeSlotsRequest.getDate());
        if (availability == null) {
            String message = messageSource.getMessage("availability.get.time-slots.failure", null, Locale.getDefault());
            throw new MBException(message);
        }

        // Filter and retrieve the time slots for the specific date
        List<TimeSlot> filteredSlots = availability.getTimeSlots();

        // You can either set the filtered time slots directly to the flow or wrap them in a response object
        flow.setData(filteredSlots);
    }
    private void handleDisplaySaveTimeSlots(DisplaySaveTimeSlotsRequest displaySaveTimeSlotsRequest) throws MBException, IOException {
        // Find the display by its ID
        Display display = displayRepository.findById(displaySaveTimeSlotsRequest.getId()).orElse(null);
        if (display == null) {
            String message = messageSource.getMessage("display.save.time-slots.failure", null, Locale.getDefault());
            throw new MBException(message);
        }

        // Check if availability for the given date already exists
        Availability existingAvailability = availabilityRepository.findByDisplayIdAndDate(display.getId(), displaySaveTimeSlotsRequest.getDate());

        if (existingAvailability != null) {
            // If availability exists, update the existing time slots
            existingAvailability.setTimeSlots(displaySaveTimeSlotsRequest.getTimeslots()
                    .stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList()));
            availabilityRepository.save(existingAvailability);
        } else {
            // If availability does not exist, create a new entry
            Availability newAvailability = new Availability();
            newAvailability.setDisplay(display);
            newAvailability.setDate(displaySaveTimeSlotsRequest.getDate());
            newAvailability.setTimeSlots(displaySaveTimeSlotsRequest.getTimeslots()
                    .stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList()));
            availabilityRepository.save(newAvailability);
        }

        // Prepare the success message
        String saveMessage = messageSource.getMessage("display.save.time-slots.success", null, Locale.getDefault());
        flow.addInfo(saveMessage);
    }

    // Helper method to convert TimeslotRequest to TimeSlot entity
    private TimeSlot convertToEntity(TimeslotRequest timeslotRequest) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(timeslotRequest.getStartTime());
        timeSlot.setEndTime(timeslotRequest.getEndTime());
        timeSlot.setStatus(timeslotRequest.getStatus());
        return timeSlot;
    }

}
