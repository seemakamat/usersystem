package com.example.usersystem.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * JPA Entity to represent "User" table.
 */
@Entity
@Table(name = "USER")
public class User {

	private static final String INVALID_FIRST_NAME = "First name must not be empty and maximum 50 characters long";
	private static final String INVALID_LAST_NAME = "Last name must be maximum 50 characters long";
	private static final String INVALID_EMAIL = "Email must be unique, non empty and maximum 100 characters long";

	@Id
	@Column(name = "USER_ID", updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdGenerator")
	@SequenceGenerator(name = "userIdGenerator", sequenceName = "SEQ_USER_ID", allocationSize = 10, initialValue = 123)
	private Long userId;

	@NotNull(message=INVALID_FIRST_NAME)
	@Size(min=1, max = 50, message = INVALID_FIRST_NAME)
	@Column(name = "FIRST_NAME", length = 50)
	private String firstName;

	@Size(max = 50, message=INVALID_LAST_NAME)
	@Column(name = "LAST_NAME", length = 50)
	private String lastName;

	@NotNull(message = INVALID_EMAIL)
	@Column(name = "EMAIL", length = 100, unique = true)
	private String email;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
