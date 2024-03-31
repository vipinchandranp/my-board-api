package com.myboard.userservice.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "dateTimeSlots")
public class DisplayTimeSlot extends SuperEntity {

	@Id
	private String id;

	private LocalDate date;

	@JsonFormat(pattern = "HH:mm")
	private LocalTime startTime;

	@JsonFormat(pattern = "HH:mm")
	private LocalTime endTime;

	@DBRef
	private User displayOwnerUser;

	@DBRef
	private User boardOwnerUser;

	@DBRef
	private Board board;

	@DBRef
	private Display display;

	private Boolean approved = false;
	

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public User getDisplayOwnerUser() {
		return displayOwnerUser;
	}

	public void setDisplayOwnerUser(User displayOwnerUser) {
		this.displayOwnerUser = displayOwnerUser;
	}

	public User getBoardOwnerUser() {
		return boardOwnerUser;
	}

	public void setBoardOwnerUser(User boardOwnerUser) {
		this.boardOwnerUser = boardOwnerUser;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}
}
