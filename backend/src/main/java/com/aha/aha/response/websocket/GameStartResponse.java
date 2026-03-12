package com.aha.aha.response.websocket;

public class GameStartResponse {

    private String roomId;
    private String message;
    
    public GameStartResponse(String roomId, String message) {
        this.roomId = roomId;
        this.message = message;
    }

    public String getRoomId() {
        return roomId;
    }
    
    public String getMessage() {
        return message;
    }
}
