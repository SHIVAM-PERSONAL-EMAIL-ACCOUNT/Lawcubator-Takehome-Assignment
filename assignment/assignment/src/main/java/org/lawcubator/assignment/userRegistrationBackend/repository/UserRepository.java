package org.lawcubator.assignment.userRegistrationBackend.repository;

import org.lawcubator.assignment.userRegistrationBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);
	User findByUsernameAndPassword(String username, String password);
}
