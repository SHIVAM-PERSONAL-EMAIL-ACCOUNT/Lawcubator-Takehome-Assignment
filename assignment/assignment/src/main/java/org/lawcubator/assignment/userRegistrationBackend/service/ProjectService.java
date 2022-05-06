package org.lawcubator.assignment.userRegistrationBackend.service;

import java.util.Set;

import org.lawcubator.assignment.userRegistrationBackend.model.Project;
import org.lawcubator.assignment.userRegistrationBackend.model.User;
import org.lawcubator.assignment.userRegistrationBackend.model.Visibility;
import org.lawcubator.assignment.userRegistrationBackend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * Service Implementation that loads Project specific data. 
 */
@Service
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final UserService userService;
	
	@Autowired
	public ProjectService(ProjectRepository projectRepository, UserService userService) {
		this.projectRepository = projectRepository;
		this.userService = userService;
	}

	/**
	 * Adds a new Project to the database owned by a User already present in the database
	 * 
	 * @param projectToBeSaved New Project to be saved in the database
	 * @return Project saved to the database
	 * @throws DataIntegrityViolationException if current Project name had already been taken by an existing 
	 * project in the database
	 */
	public Project saveProject(Project projectToBeSaved) {
		Visibility visibility = projectToBeSaved.getVisibility();
		
		if (visibility == null) {
			projectToBeSaved.setVisibility(Visibility.PUBLIC);
		}
		
		Project savedProject = projectRepository.save(projectToBeSaved);
		return savedProject;
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
	public Project findProjectById(Integer id, String currentUsername) {
		Project foundProject = projectRepository.findProjectById(id);
		
		if (foundProject == null) {
			throw new EmptyResultDataAccessException(0);
		}
		
		if (!foundProject.getUser().getUsername().equals(currentUsername) && foundProject.getVisibility() == Visibility.PRIVATE) {
			throw new IllegalArgumentException("Request is a private project of someone else");
		}
		
		return foundProject;
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
	 * @throws DataIntegrityViolationException if current Project name had already been taken by an existing 
	 * Project in the database
	 */
	public Project modifyProject(Project newProjectDetails) {
		Project projectToBeModified = findProjectById(newProjectDetails.getId(), newProjectDetails.getUser().getUsername());
		
		if (projectToBeModified == null) {
			throw new EmptyResultDataAccessException(0);
		}
		
		projectToBeModified.setName(newProjectDetails.getName());
		projectToBeModified.setDescription(newProjectDetails.getDescription());
		if (newProjectDetails.getVisibility() == null) {
			newProjectDetails.setVisibility(Visibility.PUBLIC);
		}
		projectToBeModified.setVisibility(newProjectDetails.getVisibility());
		
		Project modidfiedProject = saveProject(projectToBeModified);
		return modidfiedProject;
	}

	/**
	 * Deletes a Project present in the database with given Id
	 * 
	 * @param id Id of the Project to be deleted
	 * @throws EmptyResultDataAccessException if Project with given Id was not present in the database
	 * @throws IllegalArgumentException if the Project does not belong to the current User
	 */
	public void removeProject(Integer id, String currentUsername) {
		try {
			findProjectById(id, currentUsername);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Project does not belong to current user");
		}
		
		projectRepository.deleteById(id);		
	}

	/**
	 * Finds all the Projects currently owned by a User
	 * <p>
	 * If User who has requested the Projects and User whose Projects are being requested are same
	 * then all the projects of the User present in the database will be returned. However,
	 * if both are different, then only {@code Public} Projects of the requested User will be returned.
	 * 
	 * @param currentUsername User who is requesting the Projects
	 * @param requestedUsername User whose Projects are being requested
	 * @return Set of all the Projects owned by the requested User. If requested User is same the requesting User,
	 * all the Projects will be returned. Else, only {@code Public} Projects will be returned
	 * @throws IllegalArgumentException - if the requested User was not present in the database
	 */
	public Set<Project> findAllProjectsOfRequestedUser(String currentUsername, String requestedUsername) {
		User requestedUser = userService.findUserByUsername(requestedUsername);
		
		if (requestedUser == null) {
			throw new IllegalArgumentException("User not present in the database");
		}
		
		Set<Project> projects = projectRepository.findProjectsByUser(requestedUser);
		
		if (currentUsername.equals(requestedUsername)) {
			return projects;
		}
		
		for (Project project : projects) {
			if (project.getVisibility() == Visibility.PRIVATE) {
				projects.remove(project);
			}
		}
		
		return projects;
	}
	
	/**
	 * Finds all the {@code Public} Projects that are owned by all the Users
	 * existing in the application, except for the User requesting the Projects
	 * 
	 * @param currentUsername User who is requesting the Projects
	 * @return Set of all the Projects owned by all the Users except the requesting User
	 */
	public Set<Project> findAllPublicProjectsOfOtherUsers(String currentUsername) {
		User currentUser = userService.findUserByUsername(currentUsername);
		Set<Project> projects = projectRepository.findAllPublicProjectsOfOtherUsers(currentUser);
		return projects;
	}
}
