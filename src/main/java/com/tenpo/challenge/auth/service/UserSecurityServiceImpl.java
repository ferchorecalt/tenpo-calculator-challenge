package com.tenpo.challenge.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.tenpo.challenge.auth.JWTTokenService;
import com.tenpo.challenge.auth.exception.LoginUnsuccessfulException;

/**
 * Provide spring security user authentication feature
 *
 * @author Fermin Recalt
 */

@Service
public class UserSecurityServiceImpl implements UserSecurityService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserSecurityServiceImpl.class);
	
	private AuthenticationManager authenticationManager;
	private UserDetailsService userDetailsService;
	private JWTTokenService JWTTokenService;
	
	public UserSecurityServiceImpl(AuthenticationManager authenticationManager, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, com.tenpo.challenge.auth.JWTTokenService JWTTokenService) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.JWTTokenService = JWTTokenService;
	}
	
	@Override
	public String login(String username, String password) {
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
		
		this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		
		if (usernamePasswordAuthenticationToken.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			LOGGER.debug("Login {} successfully!", username);
			return this.JWTTokenService.getJWTToken(username);
		}
		
		String message = String.format("Login %s failed!", username);
		LoginUnsuccessfulException ex = new LoginUnsuccessfulException(message);
		LOGGER.debug(ex.getMessage());
		throw ex;
	}
	
	@Override
	public void logout(String token) {
		this.JWTTokenService.invalidateUserToken(token);
	}
}
