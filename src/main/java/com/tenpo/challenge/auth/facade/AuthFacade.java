package com.tenpo.challenge.auth.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import com.tenpo.challenge.auth.dto.UserDTO;
import com.tenpo.challenge.auth.model.User;
import com.tenpo.challenge.auth.service.UserSecurityService;
import com.tenpo.challenge.auth.service.UserService;
import com.tenpo.challenge.auth.validation.UserCreateValidator;
import com.tenpo.challenge.auth.validation.UserValidator;
import com.tenpo.challenge.exception.InvalidArgumentException;

/**
 * Authentication facade in charge of validate auth requests body and call user services
 *
 * @author Fermin Recalt
 */

@Component
public class AuthFacade {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthFacade.class);
	
	private UserService userService;
	private UserSecurityService userSecurityService;
	private UserCreateValidator userCreateValidator;
	private UserValidator userLoginValidator;
	
	public AuthFacade(UserService userService, UserSecurityService userSecurityService, UserCreateValidator userCreateValidator, @Qualifier("userValidator") UserValidator userLoginValidator) {
		this.userService = userService;
		this.userSecurityService = userSecurityService;
		this.userCreateValidator = userCreateValidator;
		this.userLoginValidator = userLoginValidator;
	}
	
	public void userSignin(UserDTO userDTO, BindingResult bindingResult) {
		this.validateInput(userDTO, this.userCreateValidator, bindingResult);
		
		User user = new User();
		user.setUsername(userDTO.getUsername());
		user.setPassword(userDTO.getPassword());
		
		this.userService.save(user);
	}
	
	public String login(UserDTO userDTO, BindingResult bindingResult) {
		this.validateInput(userDTO, this.userLoginValidator, bindingResult);
		
		return this.userSecurityService.login(userDTO.getUsername(), userDTO.getPassword());
	}
	
	private void validateInput(UserDTO userDTO, Validator validator, BindingResult bindingResult) {
		validator.validate(userDTO, bindingResult);
		if (bindingResult.hasErrors()) {
			InvalidArgumentException e = new InvalidArgumentException(bindingResult.getAllErrors().stream().findFirst().get().getCode());
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}
	
	public void logout(String token) {
		this.userSecurityService.logout(token);
	}
}
