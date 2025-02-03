package com.myboard.userservice.security;

import java.io.IOException;

import com.myboard.userservice.service.MBUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MBUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Get the request URI
        String requestUri = request.getRequestURI();

        // Exclude the filter for login, signup, and other public endpoints
        if (isPublicEndpoint(requestUri)) {
            chain.doFilter(request, response);
            return;
        }

        // Extract token from Authorization header or query parameters
        String jwt = extractToken(request);
        String username = null;

        if (jwt != null) {
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                logger.warn("Invalid JWT token: " + e.getMessage());
            }
        }

        // Authenticate the user if username is extracted and no existing authentication is present
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Determines if the request URI matches any public endpoints.
     *
     * @param requestUri the request URI
     * @return true if the URI corresponds to a public endpoint, false otherwise
     */
    private boolean isPublicEndpoint(String requestUri) {
        return requestUri.endsWith("/myboard/user/login") ||
                requestUri.endsWith("/myboard/user/signup") ||
                requestUri.endsWith("/public-endpoint"); // Add other public URIs if needed
    }

    /**
     * Extracts the JWT token from the Authorization header or query parameter.
     *
     * @param request the HttpServletRequest
     * @return the extracted JWT token, or null if not found
     */
    private String extractToken(HttpServletRequest request) {
        // Check the Authorization header first
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        // Fallback to query parameter
        String tokenParam = request.getParameter("token");
        if (tokenParam != null) {
            return tokenParam;
        }

        return null;
    }
}
