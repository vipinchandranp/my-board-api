package com.myboard.userservice.service;

import com.myboard.userservice.entity.Availability;
import com.myboard.userservice.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    public Availability getAvailability(String displayId, LocalDate date) {
        return availabilityRepository.findByDisplayIdAndDate(displayId, date);
    }

    public List<Availability> getAvailabilityForDisplay(String displayId) {
        return availabilityRepository.findByDisplayId(displayId);
    }

    public void saveAvailability(Availability availability) {
        availabilityRepository.save(availability);
    }

}
