package com.aha.aha.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.aha.aha.response.websocket.RoomUpdate;

@Service
public class RoomEventService {

    private final SimpMessagingTemplate messagingTemplate;

    public RoomEventService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastPlayerJoined(String roomId, String playerName) {
        RoomUpdate update = new RoomUpdate(roomId, "Player joined: " + playerName);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, update);
    }
    
    public void broadcastGameStarted(String roomId, String message) {
        RoomUpdate update = new RoomUpdate(roomId, message);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, update);
    }
}
