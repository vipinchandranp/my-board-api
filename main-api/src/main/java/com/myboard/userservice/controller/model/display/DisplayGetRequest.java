package com.myboard.userservice.controller.model.display;

import com.myboard.userservice.controller.model.common.MainRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DisplayGetRequest extends MainRequest {
    private String displayId;
}
