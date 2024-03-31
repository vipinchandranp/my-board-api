package com.myboard.userservice.service;

import java.util.List;

import com.myboard.userservice.dto.ApprovalIncomingRequestDTO;
import com.myboard.userservice.dto.BoardWithImageDTO;
import com.myboard.userservice.entity.User;

public interface ApprovalService {
	List<ApprovalIncomingRequestDTO> getAllApprovalIncomingRequestsForUser(User user);

	BoardWithImageDTO getBoardDetailsWithImage(String timeSlotId);
}
