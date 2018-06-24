package com.example.usersystem.config;

import org.junit.Assert;

import com.example.usersystem.dao.entity.User;

public class TestUserHelper {
	public static User getSampleUser() {
		User user = new User();
		user.setUserId(10L);
		user.setEmail("s@k.com");
		user.setFirstName("testFirstName");
		user.setLastName("Test last name");
		return user;
	}
	
	public static void assertUserValues(User expectedUser, User actualUser) {
		Assert.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
		Assert.assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
		Assert.assertEquals(expectedUser.getLastName(), actualUser.getLastName());
		Assert.assertEquals(expectedUser.getUserId(), actualUser.getUserId());
	}
}
