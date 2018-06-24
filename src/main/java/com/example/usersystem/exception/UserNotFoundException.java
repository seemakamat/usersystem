package com.example.usersystem.exception;

/**
 * Represents exception scenario when system tries to fetch / update 
 * user which doesn't exist in the system.
 */
public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException() {
		super("User not found in the system.");
	}
}
