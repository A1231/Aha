package com.aha.aha.controller;

import java.util.Map;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.aha.aha.config.websocket.RoomSessionHandshakeInterceptor;
import com.aha.aha.exception.ResourceNotFoundException;
import com.aha.aha.request.AnswerRequest;
import com.aha.aha.service.websocket.GameService;

@Controller
public class RoomWebSocketController {

    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    public RoomWebSocketController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/room/{roomId}/question")
    public void handleQuestion(@Header("simpSessionAttributes") Map<String, Object> sessionAttributes,  AnswerRequest answerRequest) {

        String roomSessionId = (String) sessionAttributes.get(RoomSessionHandshakeInterceptor.ROOM_SESSION_ATTR);
        if (roomSessionId == null) {
            throw new ResourceNotFoundException("Room session not found");
        }
       
        gameService.submitAnswer( answerRequest, roomSessionId);
        
    
    }

    

}