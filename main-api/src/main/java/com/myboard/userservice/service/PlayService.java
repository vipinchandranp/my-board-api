package com.myboard.userservice.service;

import com.myboard.userservice.entity.DisplayTimeSlot;

public interface PlayService {
	byte[] getDisplayImageForCurrentTime();

	void savePlayAudit(String displayTimeSlotId);
}
