package com.myboard.userservice.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private LoginFilter loginFilter;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> {
			cors.configurationSource(request -> {
				CorsConfiguration configuration = new CorsConfiguration();
				configuration.setAllowedOrigins(List.of("*"));
				configuration.setAllowedMethods(List.of("*"));
				configuration.setAllowedHeaders(List.of("*"));
				// Customize other CORS settings as needed
				return configuration;
			});
		})
		.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
		.addFilterBefore(loginFilter, JwtRequestFilter.class)
		.authorizeHttpRequests(authz -> authz.requestMatchers("/v1/users/login").permitAll().anyRequest().authenticated());
		return http.build();

	}

}
