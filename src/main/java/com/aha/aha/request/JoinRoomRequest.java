package com.aha.aha.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class JoinRoomRequest {
    @NotEmpty(message = "Room id is required")
    private String roomId;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Player name is required")
    private String playerName;

    public String getRoomId() {
        return roomId;
    }
    
    public String getPassword() {
        return password;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
