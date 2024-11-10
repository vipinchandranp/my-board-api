package com.myboard.userservice.controller.model.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterData {
    private String searchQuery;
    private LocalDate startDate;
    private LocalDate endDate;
    private String sortBy;
}
