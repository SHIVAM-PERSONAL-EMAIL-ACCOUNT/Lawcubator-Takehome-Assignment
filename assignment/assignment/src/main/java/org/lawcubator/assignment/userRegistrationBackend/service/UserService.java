package org.lawcubator.assignment.userRegistrationBackend.service;

import org.lawcubator.assignment.userRegistrationBackend.model.User;
import org.lawcubator.assignment.userRegistrationBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service Implementation that loads User specific data. 
 */
@Service
public class UserService {

	private final UserRepository userRepository;
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/**
	 * Registers a new User to the database
	 * 
	 * @param userToBeSaved A new User to be saved to the database
	 * @return The User registered with the provided credentials
	 * @throws DataIntegrityViolationException if the User username and password match with any other User's 
	 * credentials in the database
	 */
	public User saveUser(User userToBeSaved) {
		User savedUser = userRepository.save(userToBeSaved);
		return savedUser;
	}
	
	/**
	 * Locates a user in the database with the given username
	 * 
	 * @param username Username of the user that needs to be located
	 * @return Requested user, if found
	 */
	public User findUserByUsername(String username) {
		User foundUser = userRepository.findByUsername(username);
		return foundUser;
	}

	/**
	 * Checks if a User with given credentials is present in the database or not
	 * 
	 * @param userToBeAuthenticated User that needs to be located in the database
	 * @return User located in the database with the given credentials
	 */
	public User authenticateUser(User userToBeAuthenticated) {
		String username = userToBeAuthenticated.getUsername();
		String password = userToBeAuthenticated.getPassword();
		User authentiatedUser = userRepository.findByUsernameAndPassword(username, password);
		return authentiatedUser;
	}
}
