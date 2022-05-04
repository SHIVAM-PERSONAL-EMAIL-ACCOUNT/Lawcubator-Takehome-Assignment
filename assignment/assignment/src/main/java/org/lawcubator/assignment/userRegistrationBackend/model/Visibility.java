package org.lawcubator.assignment.userRegistrationBackend.model;

/**
 * Sets the Visibility status of a Project.
 * <p>
 * A {@code Public} project can be viewed by any user of the application. A {@code Private}
 * project can only be viewed by the user who owns the that project and not by anyone else.
 * <p>
 * If visibility status of a project has not been specified, by default, it is considered to be {@code Public} 
 */
public enum Visibility {
	PUBLIC, PRIVATE;
}
