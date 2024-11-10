package com.myboard.userservice.notification.model;

import org.springframework.web.socket.WebSocketMessage;

public class MBWebsocketMessage<T> implements WebSocketMessage {

    private T data;

    public MBWebsocketMessage(T data){
        this.data = data;
    }
    @Override
    public Object getPayload() {
        return data;
    }

    @Override
    public int getPayloadLength() {
        return 0;
    }

    @Override
    public boolean isLast() {
        return false;
    }
}
