package com.aha.aha.service.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.aha.aha.entity.Question;
import com.aha.aha.response.websocket.QuestionResponse;
import com.aha.aha.response.websocket.RoomUpdate;

@Service
public class RoomEventService {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameScheduler gameScheduler;

    public RoomEventService(SimpMessagingTemplate messagingTemplate, GameScheduler gameScheduler) {
        this.messagingTemplate = messagingTemplate;
        this.gameScheduler = gameScheduler;
    }

    public void broadcastPlayerJoined(String roomId, String playerName) {
        RoomUpdate update = new RoomUpdate(roomId, "Player joined: " + playerName);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, update);
    }
    
    public void broadcastGameStarted(String roomId, String message) {
        RoomUpdate update = new RoomUpdate(roomId, message);
        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/game-started", update);

        gameScheduler.scheduleNextQuestion(roomId);
    }

   
}
