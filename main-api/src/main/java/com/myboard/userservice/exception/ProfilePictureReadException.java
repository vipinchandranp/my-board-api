package com.myboard.userservice.exception;
public class ProfilePictureReadException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProfilePictureReadException(String message) {
        super(message);
    }

    public ProfilePictureReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
