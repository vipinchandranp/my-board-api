package com.myboard.userservice.controller.model.common;

import com.myboard.userservice.types.MediaType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MediaFile {
    private String fileName;
    private MediaType mediaType;
}
