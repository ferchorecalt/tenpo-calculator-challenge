package com.tenpo.challenge.auth.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Helper that validates current user request is authenticated
 *
 * @author Fermin Recalt
 */
public class AuthenticationContextHelper {
	
	private static final String ANONYMOUS_USER = "anonymousUser";
	
	public static boolean isAuthenticated() {
		Authentication authentication = getContextAuthentication();
		return authentication != null && !ANONYMOUS_USER.equals(authentication.getPrincipal());
	}
	
	public static String getAuthenticatedUsername() {
		Authentication authentication = getContextAuthentication();
		return authentication.getName();
	}
	
	private static Authentication getContextAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
}
