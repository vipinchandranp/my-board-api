package com.myboard.userservice.entity;

import com.myboard.userservice.types.PlayMode;
import com.myboard.userservice.types.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "timeslot") // Ensure it's mapped to the correct collection
public class Timeslot extends Base {

    @DBRef(lazy = true)
    private Display display; // Reference to the associated display

    @DBRef(lazy = true)
    private Board board; // Reference to the associated board

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime; // Start time of the timeslot

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime; // End time of the timeslot
    private StatusType status; // Status of the timeslot
    private PlayMode playMode = PlayMode.NEVER_PLAYED;

    private BigDecimal earnings; // Earnings from display in currency amount
}
