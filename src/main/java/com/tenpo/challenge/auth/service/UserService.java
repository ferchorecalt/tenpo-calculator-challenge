package com.tenpo.challenge.auth.service;

import com.tenpo.challenge.auth.model.User;

public interface UserService {
	
	void save(User user);
	
	User findByUsername(String username);
}
