package com.myboard.userservice.websocket.types;

public enum WebSocketAction {
    REGISTER_DISPLAY("register_display"),
    PLAY_CONTENT("play_content"),
    STOP_CONTENT("stop_content"),
    QR_CODE("qr_code"),
    UNKNOWN("unknown"); // For unrecognized actions
    
    private final String action;

    WebSocketAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    // Method to get enum from string (for parsing incoming messages)
    public static WebSocketAction fromString(String action) {
        for (WebSocketAction type : WebSocketAction.values()) {
            if (type.getAction().equalsIgnoreCase(action)) {
                return type;
            }
        }
        return UNKNOWN; // Return UNKNOWN if action doesn't match any predefined actions
    }
}
