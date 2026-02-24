package com.aha.aha.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value= "room", timeToLive = 86400) // 24 hours
public class Room {
    @Id
    private String roomId;
    private String topic;
    private int maxPlayers;
    private String hostId;  // User id of the host who created the room
    private List<String> players;
    private LocalDateTime createdAt;
    private String password;

   
    public Room() {
        this.players = new ArrayList<>();
    }

    public Room(String roomId, String topic, int maxPlayers, String hostId, LocalDateTime createdAt, String password) {
        this.roomId = roomId;
        this.topic = topic;
        this.maxPlayers = maxPlayers;
        this.hostId = hostId;
        this.createdAt = createdAt;
        this.password = password;
        this.players = new ArrayList<>();
        
    }

    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    
   

    
    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
    
    
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    
}
