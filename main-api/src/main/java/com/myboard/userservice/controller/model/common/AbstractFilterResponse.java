package com.myboard.userservice.controller.model.common;

import com.myboard.userservice.types.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AbstractFilterResponse {
    private String id;
    private String name;
    private ItemType itemType;
}
