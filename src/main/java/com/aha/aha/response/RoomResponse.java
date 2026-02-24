package com.aha.aha.response;

public class RoomResponse {
    
    private final Long roomId;
    private final String topic;
    private final int maxPlayers;
    private final String password;
    private final Long hostId;
    

    public RoomResponse(Long roomId, String topic, int maxPlayers,  String password, Long hostId) {
        this.roomId = roomId;
        this.topic = topic;
        this.maxPlayers = maxPlayers;
        this.password = password;
        this.hostId = hostId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getTopic() {
        return topic;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getPassword() {
        return password;
    }

    public Long getHostId() {
        return hostId;
    }

}
