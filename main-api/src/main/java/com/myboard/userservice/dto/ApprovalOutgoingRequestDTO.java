package com.myboard.userservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ApprovalOutgoingRequestDTO {
	private String id;
	private String boardId;
	private String displayId;
	private String displayOwnerUserId;
	private String displayOwnerUserName;
	private String requesterUserId;
	private String boardTitle;
	private String displayTitle;
	private LocalDate requestDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String requesterName;
	private Boolean isApproved;
}
