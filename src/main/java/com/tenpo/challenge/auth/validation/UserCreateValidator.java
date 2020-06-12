package com.tenpo.challenge.auth.validation;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import com.tenpo.challenge.auth.dto.UserDTO;
import com.tenpo.challenge.auth.service.UserService;

/**
 * Create user request body validator
 *
 * @author Fermin Recalt
 */

@Component
public class UserCreateValidator extends UserValidator {
	
	private UserService userService;
	
	public UserCreateValidator(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public void validate(Object object, Errors errors) {
		super.validate(object, errors);
		UserDTO userDTO = (UserDTO) object;
		
		if (this.userService.findByUsername(userDTO.getUsername()) != null) {
			errors.reject("Username already exists");
		}
		
		if (StringUtils.isEmpty(userDTO.getPassword())
				|| StringUtils.isEmpty(userDTO.getConfirmPassword())
				|| passwordsAreNotEqual(userDTO)) {
			errors.reject("Passwords must be equal");
		}
	}
	
	private static boolean passwordsAreNotEqual(UserDTO userDTO) {
		return !userDTO.getPassword().equals(userDTO.getConfirmPassword());
	}
}
