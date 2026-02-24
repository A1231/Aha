package com.aha.aha.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class RoomRequest {

    @NotBlank(message = "Topic is required")
    private String topic;

    @Min(2)
    @Max(10)
    private int maxPlayers;

    @NotBlank(message = "Host name is required")
    private String hostName;


   

    public RoomRequest(String hostName,String topic, int maxPlayers) {
        this.topic = topic;
        this.maxPlayers = maxPlayers;
        this.hostName = hostName;
        
    }

    public String getTopic() {
        return topic;
    }
    
    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getHostName() {
        return hostName;
    }

   
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    
}
