package com.myboard.userservice.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple broker for STOMP communication
        config.enableSimpleBroker("/topic"); // Use /topic for publish-subscribe
        config.setApplicationDestinationPrefixes("/app"); // All messages from clients will go through /app endpoint
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the WebSocket endpoint (clients will connect to this endpoint)
        registry.addEndpoint("/websocket")
                .setHandshakeHandler(new CustomHandshakeHandler());
    }
}
