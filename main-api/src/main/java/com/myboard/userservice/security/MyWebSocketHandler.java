package com.myboard.userservice.security;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);
    private ScheduledExecutorService executorService;
    private int counter = 0; // Initialize counter

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("New WebSocket connection established: " + session.getId());
        session.sendMessage(new TextMessage("Welcome to the WebSocket server!"));

        // Start sending counter messages every 3 seconds
        startCounter(session);
    }

    private void startCounter(WebSocketSession session) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            try {
                counter++; // Increment the counter
                session.sendMessage(new TextMessage("Counter: " + counter));
            } catch (Exception e) {
                logger.error("Error sending message to client: " + e.getMessage());
                stopCounter(); // Stop if there's an error
            }
        }, 0, 3, TimeUnit.SECONDS); // Start immediately, repeat every 3 seconds
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("Received message: " + message.getPayload());

        // Echo the received message back to the client
        session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        logger.info("WebSocket connection closed: " + session.getId());
        stopCounter(); // Stop the counter when the connection is closed
    }

    private void stopCounter() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown(); // Shutdown the executor service
        }
    }
}
