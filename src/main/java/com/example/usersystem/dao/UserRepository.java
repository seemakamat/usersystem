package com.example.usersystem.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.usersystem.dao.entity.User;

/**
 *  Repository to interact with user table.
 */
public interface UserRepository extends CrudRepository<User, Long>{
	
}
