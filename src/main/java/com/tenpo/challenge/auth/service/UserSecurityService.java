package com.tenpo.challenge.auth.service;

public interface UserSecurityService {
	
	String login(String username, String password);
	
	void logout(String token);
}
