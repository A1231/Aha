package com.aha.aha.controller;

import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.aha.aha.config.websocket.RoomSessionHandshakeInterceptor;
import com.aha.aha.entity.RoomSession;
import com.aha.aha.repository.RoomSessionRepository;

@Controller
public class RoomWebSocketController {

    

}