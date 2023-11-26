package com.inn.cafe.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.inn.cafe.service.CustomerUserDetailService;

import io.jsonwebtoken.Claims;

@Component
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtils utils;
	@Autowired
	private CustomerUserDetailService customerUserDetailService;

	Claims claims = null;
	private String userName = null;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// if the path matches then we'll simply pass this request as valid(No token
		// validation required)
		if (request.getServletPath().matches("/user/login|/user/sign-up|/user/forgot-password")) {
			filterChain.doFilter(request, response); // this request will no goto the SecurityConfig class
		} else {
			// do the filtration of request
			String authHeader = request.getHeader("Authorization"); // getting request Authorization header
			String token = null;

			// checking the header stuff
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7); // extract the token from request sent by client
				userName = utils.extractUsername(token); // extracting username which is email in our case thro Utils
															// class
				claims = utils.extractAllClaims(token); // extracting all claims
			}

			// checking the username extracted is not null and checking the Session also if
			// Session is null i.e no session found then get the username from DB
			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				// loaded the userDeatils(Spring-security) obj containing details like
				// Username,Pwd,Roles
				UserDetails userDetails = customerUserDetailService.loadUserByUsername(userName);

				// checking the token is valid or not
				if (utils.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePwdToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePwdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePwdToken);
				}
			}
			filterChain.doFilter(request, response); // this request will no goto the SecurityConfig class
		}
	}

	public boolean isAdmin() {
		return "admin".equalsIgnoreCase(claims.get("role") + "");
	}

	public boolean isUser() {
		return "user".equalsIgnoreCase(claims.get("role") + "");
	}

	public String getCurrentLoggedInUser() {
		return userName;
	}

}
