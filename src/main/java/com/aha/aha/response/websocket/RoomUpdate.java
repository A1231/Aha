package com.aha.aha.response.websocket;

public class RoomUpdate {

    String roomId;
    String message;

    public RoomUpdate(String roomId, String message) {
        this.roomId = roomId;
        this.message = message;
    }

    public String getRoomId() {
        return roomId;
    }
    
    public String getMessage() {
        return message;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
