package com.aha.aha.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("user")
public class User {
    @Id
    private String  id;
    private String  name;
    private int score;
    private boolean isHost;
    private String roomId;
    //private String ipAddress;

    public User() {
    }

    public User(String id, String name, int score, boolean isHost, String roomId) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.isHost = isHost;
        this.roomId = roomId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    

    
}
