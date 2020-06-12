package com.tenpo.challenge.auth.service;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.tenpo.challenge.auth.model.Role;
import com.tenpo.challenge.auth.model.RoleName;
import com.tenpo.challenge.auth.model.User;
import com.tenpo.challenge.auth.repository.RoleRepository;
import com.tenpo.challenge.auth.repository.UserRepository;

/**
 * User service in charge of perform user crud operations
 *
 * @author Fermin Recalt
 */

@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Override
	public void save(User user) {
		user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
		
		Set<Role> userRoles = new HashSet<>();
		userRoles.add(this.roleRepository.findByName(RoleName.USER));
		
		user.setRoles(userRoles);
		this.userRepository.save(user);
		
		LOGGER.debug("User {} created successfully", user.getUsername());
	}
	
	@Override
	public User findByUsername(String username) {
		return this.userRepository.findByUsername(username);
	}
}
