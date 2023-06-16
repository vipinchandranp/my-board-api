package com.myboard.userservice.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomFilter extends AbstractAuthenticationProcessingFilter {

	public CustomFilter(AuthenticationManager authenticationManager) {
		super("/**"); // Set the URL pattern for this filter
		setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		// Custom authentication logic
		// Retrieve credentials from the request and perform authentication
		// Create an Authentication object and return it
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// Perform authentication logic here...
		// For example, you can use the AuthenticationManager to authenticate the
		// credentials
		Authentication authentication = getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		return authentication;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// Custom logic after successful authentication
		// For example, you can generate and set a JWT token in the response header
		String token = generateToken(authResult); // Implement token generation logic
		response.addHeader("Authorization", "Bearer " + token);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// Custom logic after unsuccessful authentication
		// For example, you can handle authentication failure and send an appropriate
		// response
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write("Authentication failed");
	}

	// Helper method to generate a token (implement according to your requirements)
	private String generateToken(Authentication authentication) {
		// Implement token generation logic here
		return "your_generated_token";
	}
}
