package com.myboard.userservice.security;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);
    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private ScheduledExecutorService executorService;
    private int counter = 0;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("New WebSocket connection established: " + session.getId());
        sessions.add(session); // Add the session to the set
        session.sendMessage(new TextMessage("Welcome to the WebSocket server!"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        logger.info("WebSocket connection closed: " + session.getId());
        sessions.remove(session); // Remove the session from the set
    }

    // Public static method to get active sessions
    public static Set<WebSocketSession> getSessions() {
        return sessions; // Return the set of active sessions
    }
}
