package com.aha.aha.request;

import jakarta.validation.constraints.NotBlank;

public class JoinRoomRequest {
    @NotBlank(message = "Room id is required")
    private Long roomId;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Player name is required")
    private String playerName;

    public Long getRoomId() {
        return roomId;
    }
    
    public String getPassword() {
        return password;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
