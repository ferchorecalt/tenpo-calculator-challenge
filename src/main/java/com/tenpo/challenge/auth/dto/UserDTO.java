package com.tenpo.challenge.auth.dto;

/**
 * Represents user request body using through authentication
 *
 * @author Fermin Recalt
 */
public class UserDTO {
	
	private String username;
	private String password;
	private String confirmPassword;
	
	public UserDTO() {
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
