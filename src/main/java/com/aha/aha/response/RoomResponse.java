package com.aha.aha.response;

public class RoomResponse {
    
    private final String roomId;
    private final String topic;
    private final int maxPlayers;
    private final String password;
    private final String hostId;
    

    public RoomResponse(String roomId, String topic, int maxPlayers,  String password, String hostId) {
        this.roomId = roomId;
        this.topic = topic;
        this.maxPlayers = maxPlayers;
        this.password = password;
        this.hostId = hostId;
    }

    public String getRoomId() {
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

    public String getHostId() {
        return hostId;
    }

}
