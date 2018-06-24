package com.example.usersystem.service;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.usersystem.config.TestConfig;
import com.example.usersystem.config.TestUserHelper;
import com.example.usersystem.dao.UserRepository;
import com.example.usersystem.dao.entity.User;
import com.example.usersystem.exception.UserNotFoundException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserServiceTest {

	@Mock
	private User user;

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Before
	public void setupSetupMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddUser() {
		User expectedUser = TestUserHelper.getSampleUser();
		when(userRepository.save(user)).thenReturn(expectedUser);

		User actualUser = userService.add(user);
		TestUserHelper.assertUserValues(expectedUser, actualUser);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testAddUserWithDuplicateEmailThrowsException() {
		when(userRepository.save(user)).thenThrow(new DataIntegrityViolationException("Duplicate"));
		userService.add(user);
	}

	@Test
	public void testUpdateUser() {
		User expectedUser = TestUserHelper.getSampleUser();
		when(userRepository.findOne(Mockito.anyLong())).thenReturn(expectedUser);
		when(userRepository.save(user)).thenReturn(expectedUser);

		User actualUser = userService.update(user);
		TestUserHelper.assertUserValues(expectedUser, actualUser);
	}

	@Test(expected = UserNotFoundException.class)
	public void testUpdateUserThatDoesNotExists() {
		when(userRepository.findOne(Mockito.anyLong())).thenReturn(null);
		userService.update(user);
	}

	@Test
	public void testDeleteUser() {
		User expectedUser = TestUserHelper.getSampleUser();
		when(userRepository.findOne(Mockito.anyLong())).thenReturn(expectedUser);
		userService.delete(expectedUser.getUserId());
		Mockito.verify(userRepository).delete(Mockito.anyLong());
	}

	@Test(expected = UserNotFoundException.class)
	public void testDeleteUserThatDoesNotExists() {
		when(userRepository.findOne(Mockito.anyLong())).thenReturn(null);
		userService.delete(10L);
	}

	@Test
	public void testGetUser() {
		User expectedUser = TestUserHelper.getSampleUser();
		when(userRepository.findOne(Mockito.anyLong())).thenReturn(expectedUser);

		User actualUser = userService.get(user.getUserId());
		TestUserHelper.assertUserValues(expectedUser, actualUser);
	}

	@Test(expected = UserNotFoundException.class)
	public void testGetUserThatDoesNotExists() {
		when(userRepository.findOne(Mockito.anyLong())).thenReturn(null);
		userService.get(10L);
	}

	@Test
	public void testGetAllUsers() {
		User expectedUser = TestUserHelper.getSampleUser();
		when(userRepository.findAll()).thenReturn(Arrays.asList(expectedUser));

		List<User> actualUserList = userService.getAll();
		Assert.assertEquals(1, actualUserList.size());
		TestUserHelper.assertUserValues(expectedUser, actualUserList.get(0));
	}

	@Test
	public void testGetAllUsersWithZeroUsers() {
		when(userRepository.findAll()).thenReturn(Arrays.asList());
		List<User> actualUserList = userService.getAll();
		Assert.assertEquals(0, actualUserList.size());
	}

}
