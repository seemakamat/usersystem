package com.example.usersystem.service;

import java.util.List;

import com.example.usersystem.dao.entity.User;

/**
 * Represents the various interaction APIs user service
 * needs to provide.
 */
public interface UserService {
	
	public User add(User user);

	public User update(User user);

	public User get(Long id);

	public List<User> getAll ();
	
	public void delete(Long id);
}
