package com.tenpo.challenge.auth.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.tenpo.challenge.auth.dto.UserDTO;

/**
 * Validate if username and password are present for login purpose
 *
 * @author Fermin Recalt
 */

@Component
public class UserValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> aClass) {
		return UserDTO.class.equals(aClass);
	}
	
	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Username is empty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Password is empty");
	}
}
