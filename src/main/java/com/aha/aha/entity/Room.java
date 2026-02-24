package com.aha.aha.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value= "room", timeToLive = 86400) // 24 hours
public class Room {
    @Id
    private Long roomId;
    private String topic;
    private int maxPlayers;
    private Long hostId;  // User id of the host who created the room
    private List<User> players;
    private LocalDateTime createdAt;
    private String password;

   
    public Room() {}

    public Room(Long roomId, String topic, int maxPlayers, Long hostId, LocalDateTime createdAt, String password) {
        this.roomId = roomId;
        this.topic = topic;
        this.maxPlayers = maxPlayers;
        this.hostId = hostId;
        this.createdAt = createdAt;
        this.password = password;
        
    }

    public Long getRoomId() {
        return roomId;
    }
    public void setRoomId(Long roomId) {
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

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
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
    
    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    
}
