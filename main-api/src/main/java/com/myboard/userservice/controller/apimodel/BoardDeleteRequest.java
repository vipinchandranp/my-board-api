package com.myboard.userservice.controller.apimodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@Data
@EqualsAndHashCode(callSuper = true)
public class BoardDeleteRequest extends MainRequest {
}
