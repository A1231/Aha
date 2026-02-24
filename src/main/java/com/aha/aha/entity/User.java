package com.aha.aha.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("user")
public class User {
    @Id
    private Long  id;
    private String  name;
    private int score;
    private boolean isHost;
    private Long roomId;
    //private String ipAddress;

    public User() {
    }

    public User(Long id, String name, int score, boolean isHost, Long roomId) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.isHost = isHost;
        this.roomId = roomId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setIsHost(boolean isHost) {
        this.isHost = isHost;
    }


    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    

    
}
