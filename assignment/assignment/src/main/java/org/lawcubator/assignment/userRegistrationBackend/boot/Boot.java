package org.lawcubator.assignment.userRegistrationBackend.boot;

import org.lawcubator.assignment.userRegistrationBackend.model.Project;
import org.lawcubator.assignment.userRegistrationBackend.model.User;
import org.lawcubator.assignment.userRegistrationBackend.model.Visibility;
import org.lawcubator.assignment.userRegistrationBackend.service.ProjectService;
import org.lawcubator.assignment.userRegistrationBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Bootstrap some sample User and Projects in the database for development and testing purposes
 */
@Component
public final class Boot implements CommandLineRunner {

	private final UserService userService;
	private final ProjectService projectService;
	
	@Autowired
	public Boot(UserService userService, ProjectService projectService) {
		this.userService = userService;
		this.projectService = projectService;
	}

	@Override
	public void run(String... args) throws Exception {
		User user1 = new User("Username 1", "Password 1");
		User user2 = new User("Username 2", "Password 2");
		User user3 = new User("Username 3", "Password 3");
		
		saveUser(user1, user2, user3);
		
		Project project1 = new Project("Project 1", "Project Description 1", Visibility.PUBLIC, user1);
		Project project2 = new Project("Project 2", "Project Description 2", Visibility.PRIVATE, user1);
		Project project3 = new Project("Project 3", "Project Description 3", Visibility.PUBLIC, user2);
		Project project4 = new Project("Project 4", "Project Description 4", Visibility.PRIVATE, user2);
		Project project5 = new Project("Project 5", "Project Description 5", Visibility.PRIVATE, user3);
		
		saveProject(project1, project2, project3, project4, project5);
	}
	
	private void saveUser(User ...users) {
		for (User user : users) {
			userService.saveUser(user);
		}
	}
	
	private void saveProject(Project ...projects) {
		for (Project project : projects) {
			projectService.saveProject(project);
		}
	}
}
