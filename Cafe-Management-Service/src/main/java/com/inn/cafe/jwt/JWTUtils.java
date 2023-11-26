package com.inn.cafe.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTUtils {
	private static final String secretKey = "cafemangment";

	private static final int expirationTime = 1000 * 60 * 60 * 10;

	public String extractUsername(String token) {
		return extractClaims(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}

	public <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(String username, String role) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", role);
		return createToken(claims, username);
	}

	// generating the token with help of secretKey
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims) // pass the claims
				.setSubject(subject) // this is username
				.setIssuedAt(new Date(System.currentTimeMillis())) // token when was issued
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // token when it'll expire
				.signWith(SignatureAlgorithm.HS256, secretKey) // setting signature
				.compact(); // generate the token
	}

	// validate the token received if valid then we can use our API's
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
