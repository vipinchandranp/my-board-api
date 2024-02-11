package com.myboard.userservice.exception;

public class ProfilePictureNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProfilePictureNotFoundException(String message) {
		super(message);
	}

	public ProfilePictureNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
