package com.myboard.userservice.controller.model.display;

import com.myboard.userservice.controller.model.common.MainRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class DisplayDeleteRequest extends MainRequest {
    private String displayId;
    private LocalDate date;
}
