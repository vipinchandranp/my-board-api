package com.myboard.userservice.controller.model.display.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DisplayDeleteRequest {
    private String displayId;
    private LocalDate date;
}
