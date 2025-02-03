package com.myboard.userservice.websocket.config;

import com.myboard.userservice.security.JwtUtil;
import com.myboard.userservice.service.MBUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MBUserDetailsService userDetailsService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        // Check if request is an instance of ServletServerHttpRequest
        if (request instanceof ServletServerHttpRequest servletRequest) {
            String authHeader = servletRequest.getServletRequest().getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);

                // Validate the token
                String username = jwtUtil.extractUsername(jwt);
                if (username != null && jwtUtil.validateToken(jwt, userDetailsService.loadUserByUsername(username))) {
                    attributes.put("username", username); // Store username in attributes
                    return true; // Proceed with the handshake
                }
            }
        }

        // Set status to 401 if response is an instance of ServletServerHttpResponse
        if (response instanceof ServletServerHttpResponse servletResponse) {
            servletResponse.getServletResponse().setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return false; // Reject the handshake if the token is invalid
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // Optional post-handshake processing
    }
}
