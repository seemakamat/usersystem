package com.example.usersystem.exception.handler;

/**
 * Represents the response in case the API returns error
 */
public class ApiError {
	
	private int status;
	private String message;
	private String details;

	public ApiError(int status, String message, String details) {
		super();
		this.status = status;
		this.message = message;
		this.details = details;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

}
