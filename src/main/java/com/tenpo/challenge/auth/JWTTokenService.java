package com.tenpo.challenge.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Generate user JWT token and validate each jwt request token.
 * <p>
 * Besides, it has a cache where store invalidated jwt token.
 *
 * @author Fermin Recalt
 */

@Service
public class JWTTokenService {
	
	/**
	 * It could be a database, or in memory cache like Redis
	 */
	private static final Map<String, List<String>> JWTTokenBlacklistMap = new ConcurrentHashMap<>();
	
	private String secretKey;
	private int tokenExpiration;
	
	public JWTTokenService(@Value("${SECRET_KEY}") String secretKey, @Value("${TOKEN_EXPIRATION}") int tokenExpiration) {
		this.secretKey = secretKey;
		this.tokenExpiration = tokenExpiration;
	}
	
	public String getJWTToken(String username) {
		
		String token = Jwts
				.builder()
				.setClaims(new HashMap<>())
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + this.tokenExpiration))
				.signWith(SignatureAlgorithm.HS512,
						this.secretKey.getBytes()).compact();
		
		return token;
	}
	
	private Date getTokenExpirationDate(String token) {
		return this.getTokenClaim(token, Claims::getExpiration);
	}
	
	private <T> T getTokenClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = this.getAllTokenClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String getUsernameFromToken(String token) {
		return this.getTokenClaim(token, Claims::getSubject);
	}
	
	private Claims getAllTokenClaims(String token) {
		return Jwts.parser().setSigningKey(this.secretKey.getBytes()).parseClaimsJws(token).getBody();
	}
	
	private boolean isTokenExpired(String token) {
		Date expiration = this.getTokenExpirationDate(token);
		return expiration.before(new Date());
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		String username = this.getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && this.isValidToken(username, token));
	}
	
	private boolean isValidToken(String username, String token) {
		boolean isBlacklistedToken = JWTTokenBlacklistMap.containsKey(username)
				&& JWTTokenBlacklistMap.get(username).contains(token);
		
		return !this.isTokenExpired(token) && !isBlacklistedToken;
	}
	
	/**
	 * <p>
	 * Invalidate JWT token due to logout operation.
	 * <p>
	 * Save the JWT in a blacklist cache so as the same JWT cannot be used anymore
	 * </p>
	 *
	 * @param token token to invalidate
	 */
	public void invalidateUserToken(String token) {
		String username = this.getUsernameFromToken(token);
		
		JWTTokenBlacklistMap.putIfAbsent(username, new ArrayList<>());
		
		JWTTokenBlacklistMap.computeIfPresent(username, (key, currentList) -> {
			currentList.add(token);
			return currentList;
		});
	}
	
}
