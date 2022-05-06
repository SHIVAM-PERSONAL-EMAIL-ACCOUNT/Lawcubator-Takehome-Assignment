package org.lawcubator.assignment.userRegistrationBackend.controller;

import org.lawcubator.assignment.userRegistrationBackend.model.AuthenticationRequest;
import org.lawcubator.assignment.userRegistrationBackend.model.AuthenticationResponse;
import org.lawcubator.assignment.userRegistrationBackend.model.User;
import org.lawcubator.assignment.userRegistrationBackend.security.jwt.JWTUtil;
import org.lawcubator.assignment.userRegistrationBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for intercepting the User specific requests
 */
@RestController
@RequestMapping(path = "userApi/v1/")
public class UserController {

	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	private final UserService userService;

	@Autowired
	public UserController(UserService userService,
						  AuthenticationManager authenticationManager,
						  JWTUtil jwtUtil) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}
	
	/**
	 * Registers a new User to the database
	 * <p>
	 * It returns a JSON Web Token that can be used to access protected resources of the API
	 * 
	 * @param userToBeSaved A new User to be saved to the database
	 * @return JSON Web Token for the current User
	 * @throws DataIntegrityViolationException if the User username and password match with any other User's 
	 * credentials in the database
	 * @throws IllegalArgumentException if User credentials were null or empty
	 */
	@PostMapping("signup")
	private AuthenticationResponse saveUser(@RequestBody User userToBeSaved) {
		if (userToBeSaved.getPassword() == null ||
			userToBeSaved.getUsername() == null ||
			userToBeSaved.getUsername().trim().isEmpty() ||
			userToBeSaved.getPassword().trim().isEmpty()
		) {
			throw new IllegalArgumentException("Password or Username cannot be null or empty");
		}
		
		try {
			User savedUser = userService.saveUser(userToBeSaved);
			AuthenticationResponse jwtResponse = authenticateUser(new AuthenticationRequest(savedUser.getUsername(), savedUser.getPassword()));
			return jwtResponse;
		} catch (DataIntegrityViolationException ex) {
			throw new DataIntegrityViolationException("Username or Password has already been taken");
		}
	}
	
	/**
	 * Checks if a User with given credentials is present in the database or not. 
	 * <p>
	 * It returns a JSON Web Token upon successful authentication.
	 * 
	 * @param userToBeAuthenticated User that needs to be located in the database
	 * @return JSON Web Token for the current User
	 */
	
	@PostMapping("login")
	private AuthenticationResponse authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		UserDetails userDetails = userService.loadUserByUsername(username);
		String jwt = jwtUtil.generateToken(userDetails);
		AuthenticationResponse jwtResponse = new AuthenticationResponse(jwt);
		return jwtResponse;
	}
}
