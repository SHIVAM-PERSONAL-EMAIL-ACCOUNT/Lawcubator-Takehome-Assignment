package org.lawcubator.assignment.userRegistrationBackend.security.model;

/**
 * Represents the JSON Web Token sent by the server to the User upon
 * successful authentication
 */
public class AuthenticationResponse {

	private String jwt;

	public AuthenticationResponse(String jwt) {
		this.jwt = jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}

	@Override
	public String toString() {
		return "AuthenticationResponse [jwt=" + jwt + "]";
	}
}
