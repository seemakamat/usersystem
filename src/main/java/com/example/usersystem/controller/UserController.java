package com.example.usersystem.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.usersystem.dao.entity.User;
import com.example.usersystem.service.UserService;

/**
 * Controller for all end-points starting with "/users/"
 */
@RestController
@RequestMapping(value = UserController.BASE_URL)
public class UserController {

	@Autowired
	private UserService userService;

	public static final String BASE_URL = "/api/v1/users";

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public User add(@Valid @RequestBody User user) {
		return userService.add(user);
	}

	@PutMapping(value = "/{userId}")
	public User update(@PathVariable Long userId, @Valid @RequestBody User user) {
		user.setUserId(userId);
		return userService.update(user);
	}

	@GetMapping(value = "/{userId}")
	public User get(@PathVariable Long userId) {
		return userService.get(userId);
	}

	@DeleteMapping(value = "/{userId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long userId) {
		userService.delete(userId);
	}

	@GetMapping
	public List<User> get() {
		return userService.getAll();
	}
}
