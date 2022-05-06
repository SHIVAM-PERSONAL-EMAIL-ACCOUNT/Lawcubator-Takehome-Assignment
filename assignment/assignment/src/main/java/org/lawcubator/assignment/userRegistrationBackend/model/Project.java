package org.lawcubator.assignment.userRegistrationBackend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Represents a Project that is going to be owned by an application User. 
 * <p>
 * A Project has {@code many-to-one} relationship with a User
 */
@Entity
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	private String description;
	
	@Enumerated(EnumType.STRING)
	private Visibility visibility;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private User user;
	
	public Project() {
		
	}

	public Project(String name, String description, Visibility visibility, User user) {
		this.name = name;
		this.description = description;
		this.visibility = (visibility == null) ? Visibility.PUBLIC : visibility;
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public User getUser() {
		return user;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", description=" + description + ", visibility=" + visibility
				+ ", user=" + user + "]";
	}
}
