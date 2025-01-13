package com.myboard.userservice.websocket.config;

import com.myboard.userservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null) {
            attributes.put("Authorization", authHeader);
        }
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication; // Attach the authenticated Principal to the WebSocket session
        }
        return null; // No authenticated user
    }
}

