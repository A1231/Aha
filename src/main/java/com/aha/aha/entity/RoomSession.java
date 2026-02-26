package com.aha.aha.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("room_session")
public class RoomSession {
    @Id
    private String roomSessionId;
    
    private String roomId;
    private String userId;
    private String role;
    @TimeToLive
    private Long timeToLive;
    

    public RoomSession(String roomSessionId, String roomId, String userId, String role, Long timeToLive) {
        this.roomSessionId = roomSessionId;
        this.roomId = roomId;
        this.userId = userId;
        this.role = role;
        this.timeToLive = timeToLive;
        
    }
    
    public String getRoomSessionId() {
        return roomSessionId;
    }
    
    public String getRoomId() {
        return roomId;
    }
    
   
    
    public String getRole() {
        return role;
    }
    
    public Long getTimeToLive() {
        return timeToLive;
    }
    
    public void setTimeToLive(Long timeToLive) {
        this.timeToLive = timeToLive;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    

    
    public void setRole(String role) {
        this.role = role;
    }
    
   
    
   
}
