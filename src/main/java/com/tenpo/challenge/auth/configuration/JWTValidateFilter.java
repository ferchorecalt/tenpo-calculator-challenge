package com.tenpo.challenge.auth.configuration;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.tenpo.challenge.auth.JWTTokenService;
import io.jsonwebtoken.ExpiredJwtException;

/**
 * Filter that authenticates user request using jwt token
 *
 * @author Fermin Recalt
 */
public class JWTValidateFilter extends OncePerRequestFilter {
	
	private JWTTokenService JWTTokenService;
	private UserDetailsService userDetailsService;
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTValidateFilter.class);
	
	public JWTValidateFilter(JWTTokenService JWTTokenService, UserDetailsService userDetailsService) {
		this.JWTTokenService = JWTTokenService;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		String token = httpServletRequest.getHeader("Authorization");
		String username = this.getUsernameFromToken(token);
		this.authenticateWithToken(username, token, httpServletRequest);
		
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}
	
	private String getUsernameFromToken(String token) {
		if (token != null) {
			try {
				return this.JWTTokenService.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				String message = "Invalid JWT Token";
				LOGGER.error(message, e);
			} catch (ExpiredJwtException e) {
				String message = "Expired JWT Token";
				LOGGER.error(message, e);
			}
		}
		
		return null;
	}
	
	private void authenticateWithToken(String username, String token, HttpServletRequest httpServletRequest) {
		if (username != null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			
			if (this.JWTTokenService.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
	}
}

