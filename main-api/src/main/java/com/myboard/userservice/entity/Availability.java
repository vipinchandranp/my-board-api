package com.myboard.userservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "availability")
public class Availability {
    @Id
    private String id;
    @DBRef
    private Display display;
    @DBRef
    private Board board;
    private LocalDate date;
    private List<TimeSlot> timeSlots;
}
