package com.aha.aha.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aha.aha.entity.User;
import com.aha.aha.request.JoinRoomRequest;
import com.aha.aha.request.QuestionRequest;
import com.aha.aha.request.QuestionSetRequest;
import com.aha.aha.request.RoomRequest;
import com.aha.aha.response.RoomResponse;
import com.aha.aha.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.Map;


@Tag(name="Room REST API Endpoints", description = "Operations related to info about rooms")
@RestController
public class Controller {

    
    private final RoomService roomService;

    public Controller(RoomService roomService) {
        this.roomService = roomService;
    }


    @GetMapping("/")
    public String home() {
        return "home";
    }


    @Operation(summary = "Create a new room", description = "Create a new room with the given host name, topic, and max players")
    @PostMapping("/api/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse createRoom(HttpSession session, @RequestBody @Valid RoomRequest roomRequest) {
        RoomResponse roomResponse = roomService.createRoom(roomRequest.getHostName(), roomRequest.getTopic(), roomRequest.getMaxPlayers());

        session.setAttribute("roomId", roomResponse.getRoomId());
        session.setAttribute("hostId", roomResponse.getHostId());
        session.setAttribute("role", "HOST");

        return roomResponse;
    }

    @Operation(summary = "Join a room", description = "Join a room with the given room id and password")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/api/rooms/join")
    public void joinRoom(HttpSession session, @RequestBody @Valid JoinRoomRequest joinRoomRequest) {
        User newPlayer = roomService.joinRoom(joinRoomRequest.getRoomId(), joinRoomRequest.getPassword(), joinRoomRequest.getPlayerName());

        session.setAttribute("playerId", newPlayer.getId());
        session.setAttribute("role", "PLAYER");
    }

    @Operation(summary = "Add questions to a room", description = "Add a question to a room with the given room id and question. Only the host can add questions.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/rooms/questions")
    public void addQuestionsToRoom(HttpSession session, @RequestBody @Valid QuestionSetRequest questionSetRequest) {
        if (!"HOST".equals(session.getAttribute("role"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the host can add questions");
        }
        roomService.addQuestionsToRoom(questionSetRequest.getQuestions());
    }
    
}

