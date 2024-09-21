package com.myboard.userservice.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import jakarta.servlet.http.HttpServletResponse;
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
				}).csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(loginFilter, JwtRequestFilter.class)
				.addFilterAfter(logoutFilter(), LogoutFilter.class)
				.logout(logout -> logout.invalidateHttpSession(true).deleteCookies("remove")
						.logoutSuccessHandler((request, response, authentication) -> {
							// Do any cleanup or return a success message
							response.setStatus(HttpServletResponse.SC_OK);
							response.getWriter().flush();
						}))
				.authorizeHttpRequests(authz -> authz
						.requestMatchers("/user/login", "/user/signup","/error","/file/**").permitAll()
						.anyRequest().authenticated());

		return http.build();
	}

	@Bean
	public LogoutFilter logoutFilter() {
		LogoutFilter logoutFilter = new LogoutFilter("/", new SecurityContextLogoutHandler());
		logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		return logoutFilter;
	}
}
