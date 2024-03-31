package com.myboard.userservice.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

public abstract class SuperEntity {
	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime lastModifiedAt;

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}
}
