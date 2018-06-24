package com.example.usersystem.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.usersystem.dao.UserRepository;
import com.example.usersystem.dao.entity.User;
import com.example.usersystem.exception.UserNotFoundException;
import com.example.usersystem.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public User add(User user) {
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public User update(User user) {
		// This is to return error in case non existing user is being updated.
		get(user.getUserId());
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public User get(Long id) {
		User dbUser = userRepository.findOne(id);
		if (dbUser == null) {
			throw new UserNotFoundException();
		}
		return dbUser;
	}

	@Override
	@Transactional
	public void delete(Long id) {
		// This is to return error in case non existing user is being updated.
		get(id);
		userRepository.delete(id);
	}

	@Override
	@Transactional
	public List<User> getAll() {
		return (List<User>) userRepository.findAll();
	}
}
