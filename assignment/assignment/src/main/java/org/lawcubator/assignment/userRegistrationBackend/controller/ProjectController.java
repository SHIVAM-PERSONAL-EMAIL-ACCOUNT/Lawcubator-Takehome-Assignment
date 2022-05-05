package org.lawcubator.assignment.userRegistrationBackend.controller;

import java.util.Set;

import org.lawcubator.assignment.userRegistrationBackend.model.Project;
import org.lawcubator.assignment.userRegistrationBackend.model.User;
import org.lawcubator.assignment.userRegistrationBackend.service.ProjectService;
import org.lawcubator.assignment.userRegistrationBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that is responsible for intercepting the Project specific requests
 */
@RestController
@RequestMapping(path = "projectApi/v1/")
public class ProjectController {

	private final ProjectService projectService;
	private final UserService userService;

	@Autowired
	public ProjectController(ProjectService projectService, UserService userService) {
		this.projectService = projectService;
		this.userService = userService;
	}
	
	/**
	 * Adds a new project to the database owned by a User already present in the database
	 * 
	 * @param projectToBeSaved New Project to be saved in the database
	 * @return Project saved to the database
	 * @throws DataIntegrityViolationException if current Project name had already been taken by an existing 
	 * project in the database
	 * @throws IllegalArgumentException if the proposed Project name is null or empty or User of Project to be 
	 * saved is not same as the current User
	 */
	@PostMapping("project/new")
	private Project saveProject(@RequestBody Project projectToBeSaved, 
				    @RequestParam("user") String currentUsername) {
		if (projectToBeSaved.getUser() == null) {
			throw new IllegalArgumentException("User credentials are invalid");
		}
		
		User currentUser = userService.findUserByUsername(currentUsername);
		
		if (currentUser.getId() != projectToBeSaved.getUser().getId() ||
			!currentUser.getUsername().equals(projectToBeSaved.getUser().getUsername()) ||
			!currentUser.getPassword().equals(projectToBeSaved.getUser().getPassword())) {
			throw new IllegalArgumentException("User credentials are invalid");
		}
		
		String name = projectToBeSaved.getName();
		
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Project name cannot be null or empty");
		}
		
		try {
			Project savedProject = projectService.saveProject(projectToBeSaved);
			return savedProject; 
		} catch (DataIntegrityViolationException ex) {
			throw new DataIntegrityViolationException("Project name has already been taken. Try a new one.");
		}
	}
	
	/**
	 * Finds all the {@code Public} Projects that are owned by all the Users
	 * existing in the application, except for the User requesting the Projects
	 * 
	 * @param currentUsername User who is requesting the Projects
	 * @return Set of all the Projects owned by all the Users except the requesting User
	 * @throws IllegalArgumentException - if the requested User was not present in the database
	 */
	@GetMapping("other-projects/all")
	private Set<Project> findAllPublicProjectsOfOtherUsers(@RequestParam("currentUser") String currentUsername) {
		Set<Project> projects = projectService.findAllPublicProjectsOfOtherUsers(currentUsername);
		return projects;
	}
	
	/**
	 * Finds all the Projects currently owned by a User
	 * <p>
	 * If User who has requested the Projects and user whose Projects are being requested are same
	 * then all the projects of the User present in the database will be returned. However,
	 * if both are different, then only {@code Public} Projects of the requested User will be returned.
	 * 
	 * @param currentUsername User who is requesting the Projects
	 * @param requestedUsername User whose Projects are being requested
	 * @return Set of all the Projects owned by the requested User. If requested User is same the requesting User,
	 * all the Projects will be returned. Else, only {@code Public} Projects will be returned
	 * @throws IllegalArgumentException - if the requested User was not present in the database
	 */
	@GetMapping("projects/all")
	private Set<Project> findAllProjectsOfRequestedUser(@RequestParam("currentUser") String currentUsername, 
							    @RequestParam("requestedUser") String requestedUsername) {
		Set<Project> projects = projectService.findAllProjectsOfRequestedUser(currentUsername, requestedUsername);
		return projects;
	}
	
	/**
	 * Locates a Project in the database with requested Id requested by a User
	 * <p>
	 * The requested Project should either belong to the User requesting the Project, or else, 
	 * it's visibility should be set to {@code Public}.
	 * 
	 * @param id Id of the Project that needs to be located
	 * @param currentUsername User requesting the Project details
	 * @return The requested Project
	 * @throws IllegalArgumentException if the requested Project did not belong to the requested User and it's
	 * visibility was set to {@code Private}
	 * @throws EmptyResultDataAccessException if Project with given Id was not present in the database
	 */
	@GetMapping("project")
	private Project findProjectById(@RequestParam("currentUser") String currentUsername, 
					@RequestParam("projectId") Integer id) {	
		Project project = projectService.findProjectById(id, currentUsername);
		return project;
	}
	
	/**
	 * Modifies the details of of the requested Project.
	 * <p>
	 * Only Project description and Visibility can be modified.
	 * User(Owner) credentials of the project are not allowed to be modified.
	 * New Name of the Project should be chosen such that it is already not used by any existing Project 
	 * in the database.
	 * 
	 * @param newProjectDetails Project with new credentials
	 * @return The modified Project whose credentials have been modified
	 * @throws EmptyResultDataAccessException if Project with given Id was not present in the database
	 * @throws IllegalArgumentException if User credentials of the project were attempted to be modified or 
	 * if the proposed Project name was null or empty or proposed User is null or is not present in the database
	 * @throws DataIntegrityViolationException if current Project name had already been taken by an existing 
	 * Project in the database
	 */
	@PutMapping("project/modify")
	private Project modifyProject(@RequestBody Project newProjectDetails, 
				      @RequestParam("user") String currentUsername) {
		if (newProjectDetails.getUser() == null) {
			throw new IllegalArgumentException("User credentials are invalid");
		}
		
		User currentUser = userService.findUserByUsername(currentUsername);
		
		if (currentUser.getId() != newProjectDetails.getUser().getId() ||
			!currentUser.getUsername().equals(newProjectDetails.getUser().getUsername()) ||
			!currentUser.getPassword().equals(newProjectDetails.getUser().getPassword())) {
			throw new IllegalArgumentException("User details cannot be modified");
		}
		
		String name = newProjectDetails.getName();
		
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Project name cannot be null or empty");
		}
		
		try {
			Project modifiedProject = projectService.modifyProject(newProjectDetails);
			return modifiedProject;
		} catch (DataIntegrityViolationException ex) {
			throw new DataIntegrityViolationException("Project name has already been taken. Try a new one.");
		} catch (EmptyResultDataAccessException ex) {
			throw ex;
		}
	}
	
	/**
	 * Deletes a Project present in the database with given Id
	 * 
	 * @param id Id of the Project to be deleted
	 * @throws EmptyResultDataAccessException if Project with given Id was not present in the database
	 */
	@DeleteMapping("project/remove")
	private void removeProject(@RequestParam("projectId") Integer id) {
		projectService.removeProject(id);
	}
}
