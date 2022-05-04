package org.lawcubator.assignment.userRegistrationBackend.repository;

import java.util.Set;

import org.lawcubator.assignment.userRegistrationBackend.model.Project;
import org.lawcubator.assignment.userRegistrationBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

	@Query("SELECT project FROM Project project JOIN FETCH project.user WHERE project.user != :user AND project.visibility = 'PUBLIC'")
	Set<Project> findAllPublicProjectsOfOtherUsers(@Param("user") User currentUser);

	@Query("SELECT project FROM Project project JOIN FETCH project.user WHERE project.user = :user")
	Set<Project> findProjectsByUser(@Param("user") User user);

	@Query("SELECT project FROM Project project JOIN FETCH project.user WHERE project.id = :id")
	Project findProjectById(@Param("id") Integer id); 
}
