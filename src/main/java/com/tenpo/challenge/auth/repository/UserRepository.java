package com.tenpo.challenge.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tenpo.challenge.auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);
}
