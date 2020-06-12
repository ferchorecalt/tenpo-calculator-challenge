package com.tenpo.challenge.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tenpo.challenge.annotation.TransactionRecorder;
import com.tenpo.challenge.auth.dto.UserDTO;
import com.tenpo.challenge.auth.facade.AuthFacade;

/**
 * User authentication controller methods
 *
 * @author Fermin Recalt
 */

@RestController
@RequestMapping("auth")
public class AuthController {
	
	private static final String SIGN_UP = "SIGN_UP";
	private static final String LOGIN = "LOGIN";
	private static final String LOGOUT = "LOGOUT";
	private static final String AUTHORIZATION = "Authorization";
	
	private AuthFacade authFacade;
	
	public AuthController(AuthFacade authFacade) {
		this.authFacade = authFacade;
	}
	
	@TransactionRecorder(transactionName = SIGN_UP)
	@PostMapping("/signup")
	public ResponseEntity<Object> signup(@RequestBody UserDTO userDTO, BindingResult bindingResult) {
		this.authFacade.userSignin(userDTO, bindingResult);
		return ResponseEntity.ok().build();
	}
	
	@TransactionRecorder(transactionName = LOGIN)
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserDTO userDTO, BindingResult bindingResult) {
		String token = this.authFacade.login(userDTO, bindingResult);
		
		return ResponseEntity.ok(token);
	}
	
	@TransactionRecorder(transactionName = LOGOUT)
	@GetMapping({"/logout"})
	public ResponseEntity<Object> logout(@RequestHeader(AUTHORIZATION) String token) {
		this.authFacade.logout(token);
		return ResponseEntity.ok().build();
	}
}
