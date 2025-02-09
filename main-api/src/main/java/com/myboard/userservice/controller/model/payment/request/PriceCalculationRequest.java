package com.myboard.userservice.controller.model.payment.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceCalculationRequest {
    private String displayId;
    private List<String> timeSlots;
    private String date;
}
