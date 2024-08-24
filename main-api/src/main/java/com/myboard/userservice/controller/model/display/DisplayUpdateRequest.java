package com.myboard.userservice.controller.model.display;

import com.myboard.userservice.controller.model.common.MainRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@Data
@EqualsAndHashCode(callSuper = true)
public class DisplayUpdateRequest extends MainRequest {
    private String displayId;
    private MultipartFile mediaContent;
}
