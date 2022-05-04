package org.lawcubator.assignment.userRegistrationBackend.controller;

import org.lawcubator.assignment.userRegistrationBackend.model.User;
import org.lawcubator.assignment.userRegistrationBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that is responsible for intercepting the User specific requests
 */
@RestController
@RequestMapping(path = "userApi/v1/")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Registers a new User to the database
	 * 
	 * @param userToBeSaved A new User to be saved to the database
	 * @return The User registered with the provided credentials
	 * @throws DataIntegrityViolationException if the User username and password match with any other User's 
	 * credentials in the database
	 * @throws IllegalArgumentException if User credentials were null or empty
	 */
	@PostMapping("signup")
	private User saveUser(@RequestBody User userToBeSaved) {
		if (userToBeSaved.getPassword() == null ||
			userToBeSaved.getUsername() == null ||
			userToBeSaved.getUsername().trim().isEmpty() ||
			userToBeSaved.getPassword().trim().isEmpty()
		) {
			throw new IllegalArgumentException("Password or Username cannot be null or empty");
		}
		
		try {
			User savedUser = userService.saveUser(userToBeSaved);
			return savedUser;
		} catch (DataIntegrityViolationException ex) {
			throw new DataIntegrityViolationException("Username or Password has already been taken");
		}
	}
	
	/**
	 * Checks if a User with given credentials is present in the database or not
	 * 
	 * @param userToBeAuthenticated User that needs to be located in the database
	 * @return User located in the database with the given credentials
	 * @throws IllegalArgumentException if User credentials were null or empty
	 * @throws IllegalArgumentException if User was not found in the database	 
	 */
	@PostMapping("login")
	private User authenticateUser(@RequestBody User userToBeAuthenticated) {
		if (userToBeAuthenticated.getPassword() == null ||
			userToBeAuthenticated.getUsername() == null ||
			userToBeAuthenticated.getUsername().trim().isEmpty() ||
			userToBeAuthenticated.getPassword().trim().isEmpty()
		) {
			throw new IllegalArgumentException("Password or Username cannot be null or empty");
		}
		
		User authenticatedUser = userService.authenticateUser(userToBeAuthenticated);
		
		if (authenticatedUser == null) {
			throw new IllegalArgumentException("User not present in the database");
		}
		
		return authenticatedUser;
	}
}
