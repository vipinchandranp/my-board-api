package com.myboard.userservice.controller.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@Data
@EqualsAndHashCode(callSuper = true)
public class DisplayUpdateRequest extends MainRequest {
    private MultipartFile mediaContent;
}
