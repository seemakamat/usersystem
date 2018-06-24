package com.example.usersystem.controller;

import static com.example.usersystem.config.TestUserHelper.getSampleUser;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.usersystem.config.TestUserHelper;
import com.example.usersystem.dao.entity.User;
import com.example.usersystem.exception.UserNotFoundException;
import com.example.usersystem.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	private static final String BASE_URL = "/api/v1/users";
	private static final String USER_URL = BASE_URL + "/10";

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetUserById() throws Exception {
		User user = getSampleUser();
		when(userService.get(Mockito.anyLong())).thenReturn(user);
		MvcResult response = mockMvc.perform(get(USER_URL)).andReturn();
		Assert.assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());
		User returnedUser = getObjectFromJSONResponse(response.getResponse().getContentAsString(), User.class);
		TestUserHelper.assertUserValues(user, returnedUser);
		Mockito.verify(userService).get(Mockito.anyLong());
		Mockito.verifyNoMoreInteractions(userService);
	}

	@Test
	public void testGetUserByIdForInvalidId() throws Exception {
		when(userService.get(Mockito.anyLong())).thenThrow(new UserNotFoundException());
		MvcResult response = mockMvc.perform(get(USER_URL)).andReturn();
		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getResponse().getStatus());
		Mockito.verify(userService).get(Mockito.anyLong());
		Mockito.verifyNoMoreInteractions(userService);
	}

	@Test
	public void testGetAllUsers() throws Exception {
		User user = getSampleUser();
		when(userService.getAll()).thenReturn(Arrays.asList(user));
		MvcResult response = mockMvc.perform(get(BASE_URL)).andReturn();
		Assert.assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());
		JSONArray objArray = new JSONArray(response.getResponse().getContentAsString());
		User returnedUser = getObjectFromJSONResponse(objArray.get(0).toString(), User.class);
		TestUserHelper.assertUserValues(user, returnedUser);
		Mockito.verify(userService).getAll();
		Mockito.verifyNoMoreInteractions(userService);
	}

	@Test
	public void testGetAllUsersWithZeroUsers() throws Exception {
		when(userService.getAll()).thenReturn(Arrays.asList());
		MvcResult response = mockMvc.perform(get(BASE_URL)).andReturn();
		Assert.assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());
		JSONArray objArray = new JSONArray(response.getResponse().getContentAsString());
		Assert.assertTrue(objArray.length() == 0);
		Mockito.verify(userService).getAll();
		Mockito.verifyNoMoreInteractions(userService);
	}

	@Test
	public void testAddUser() throws Exception {
		User user = getSampleUser();
		when(userService.add(Mockito.any())).thenReturn(user);
		MvcResult response = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andReturn();
		Assert.assertEquals(HttpStatus.CREATED.value(), response.getResponse().getStatus());
		User returnedUser = getObjectFromJSONResponse(response.getResponse().getContentAsString(), User.class);
		TestUserHelper.assertUserValues(user, returnedUser);
		Mockito.verify(userService).add(Mockito.any());
		Mockito.verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void testAddUserWithDuplicateEmail() throws Exception {
		User user = getSampleUser();
		when(userService.add(Mockito.any())).thenThrow(new DataIntegrityViolationException("DUPLICATE"));
		MvcResult response = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getResponse().getStatus());
		Mockito.verify(userService).add(Mockito.any());
		Mockito.verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void testAddUserWithNoEmail() throws Exception {
		User user = getSampleUser();
		user.setEmail(null);
		MvcResult response = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getResponse().getStatus());
	}
	
	@Test
	public void testAddUserWithLongFirstName() throws Exception {
		User user = getSampleUser();
		user.setFirstName("1234567890123456789012345678901234567890123456789012");
		MvcResult response = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getResponse().getStatus());
	}
	
	@Test
	public void testAddUserWithLongLastName() throws Exception {
		User user = getSampleUser();
		user.setLastName("1234567890123456789012345678901234567890123456789012");
		MvcResult response = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getResponse().getStatus());
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		User user = getSampleUser();
		when(userService.update(Mockito.any())).thenReturn(user);
		MvcResult response = mockMvc.perform(put(USER_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andReturn();
		Assert.assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());
		User returnedUser = getObjectFromJSONResponse(response.getResponse().getContentAsString(), User.class);
		TestUserHelper.assertUserValues(user, returnedUser);
		Mockito.verify(userService).update(Mockito.any());
		Mockito.verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void testUpdateUserForInvalidId() throws Exception {
		User user = getSampleUser();
		when(userService.update(Mockito.any())).thenThrow(new UserNotFoundException());
		MvcResult response = mockMvc.perform(put(USER_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andReturn();
		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getResponse().getStatus());
		Mockito.verify(userService).update(Mockito.any());
		Mockito.verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void testUpdateUserWithNoEmail() throws Exception {
		User user = getSampleUser();
		user.setEmail(null);
		MvcResult response = mockMvc.perform(put(USER_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getResponse().getStatus());
	}
	
	@Test
	public void testUpdateUserWithDuplicateEmail() throws Exception {
		User user = getSampleUser();
		when(userService.update(Mockito.any())).thenThrow(new DataIntegrityViolationException("DUPLICATE"));
		MvcResult response = mockMvc.perform(put(USER_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getResponse().getStatus());
		Mockito.verify(userService).update(Mockito.any());
		Mockito.verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void testUpdateUserWithLongFirstName() throws Exception {
		User user = getSampleUser();
		user.setFirstName("1234567890123456789012345678901234567890123456789012");
		MvcResult response = mockMvc.perform(put(USER_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getResponse().getStatus());
	}
	
	@Test
	public void testUpdateUserWithLongLastName() throws Exception {
		User user = getSampleUser();
		user.setLastName("1234567890123456789012345678901234567890123456789012");
		MvcResult response = mockMvc.perform(put(USER_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getResponse().getStatus());
	}
	
	@Test
	public void testDeleteUser() throws Exception {
		MvcResult response = mockMvc.perform(delete(USER_URL)).andReturn();
		Assert.assertEquals(HttpStatus.NO_CONTENT.value(), response.getResponse().getStatus());
	}
	
	@Test
	public void testDeleteUserForInvalidId( ) throws Exception {
		doThrow(new UserNotFoundException()).when(userService).delete(Mockito.anyLong());
		MvcResult response = mockMvc.perform(delete(USER_URL)).andReturn();
		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getResponse().getStatus());
	}

	private static <T> T getObjectFromJSONResponse(String response, Class<T> class1)
			throws IOException, JsonParseException, JsonMappingException, UnsupportedEncodingException {
		ObjectMapper objectMapper = new ObjectMapper();
		T obj = objectMapper.readValue(response, class1);
		return obj;
	}

	private static byte[] asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final byte[] jsonContent = mapper.writeValueAsBytes(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
