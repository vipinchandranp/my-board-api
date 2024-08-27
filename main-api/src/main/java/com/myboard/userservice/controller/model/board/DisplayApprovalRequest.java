package com.myboard.userservice.controller.model.board;

import com.myboard.userservice.controller.model.common.MainRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DisplayApprovalRequest extends MainRequest {
    private String displayId;
    private boolean approve;
}
