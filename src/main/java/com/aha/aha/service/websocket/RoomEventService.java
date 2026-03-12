package com.aha.aha.service.websocket;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.aha.aha.entity.Question;
import com.aha.aha.entity.Room;
import com.aha.aha.response.websocket.GameStartResponse;
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

    public void broadcastPlayerJoined(String roomId, List<String> players) {
        
        RoomUpdate update = new RoomUpdate(players);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, update);
    }
    
    public void broadcastGameStarted(String roomId, String message) {
        GameStartResponse response = new GameStartResponse(roomId, message);
        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/game-started", response);

        gameScheduler.scheduleNextQuestion(roomId);
    }

   
}
