package com.aha.aha.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aha.aha.entity.Room;
import com.aha.aha.entity.RoomSession;
import com.aha.aha.entity.User;
import com.aha.aha.repository.RoomRepository;
import com.aha.aha.repository.RoomSessionRepository;
import com.aha.aha.request.JoinRoomRequest;
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

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;



@Tag(name="Room REST API Endpoints", description = "Operations related to info about rooms")
@RestController
public class Controller {

    
    private final RoomService roomService;
    private final RoomSessionRepository roomSessionRepository;
    private final RoomRepository roomRepository;

    public Controller(RoomService roomService, RoomSessionRepository roomSessionRepository, RoomRepository roomRepository) {
        this.roomService = roomService;
        this.roomSessionRepository = roomSessionRepository;
        this.roomRepository = roomRepository;
    }


    @GetMapping("/")
    public String home() {
        return "home";
    }


    @Operation(summary = "Create a new room", description = "Create a new room with the given host name, topic, and max players")
    @PostMapping("/api/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse createRoom( @RequestBody @Valid RoomRequest roomRequest, HttpServletResponse response) {
        RoomResponse roomResponse = roomService.createRoom(roomRequest.getHostName(), roomRequest.getTopic(), roomRequest.getMaxPlayers(), response);

        
        return roomResponse;
    }

    @Operation(summary = "Join a room", description = "Join a room with the given room id and password")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/api/rooms/join")
    public void joinRoom(@RequestBody @Valid JoinRoomRequest joinRoomRequest, HttpServletResponse response) {
        User newPlayer = roomService.joinRoom(joinRoomRequest.getRoomId(), joinRoomRequest.getPassword(), joinRoomRequest.getPlayerName(), response);

        
    }

    @Operation(summary = "Add questions to a room", description = "Add a question to a room with the given room id and question. Only the host can add questions.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/rooms/questions")
    public void addQuestionsToRoom(@CookieValue("ROOM_SESSION") String roomSessionId, @RequestBody @Valid QuestionSetRequest questionSetRequest) {
        RoomSession roomSession = roomSessionRepository.findByRoomSessionId(roomSessionId).orElseThrow(() -> new RuntimeException("Room session not found"));
        Room room = roomRepository.findById(roomSession.getRoomId()).orElseThrow(() -> new RuntimeException("Room not found"));
        if (!"HOST".equals(roomSession.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the host can add questions");
        }
        roomService.addQuestionsToRoom(questionSetRequest.getQuestions(), room);
    }
    
    @Operation(summary = "Start the game", description = "Start the game for the given room id")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/api/rooms/start")
    public void startQuiz(@CookieValue("ROOM_SESSION") String roomSessionId) {
        RoomSession roomSession = roomSessionRepository.findByRoomSessionId(roomSessionId).orElseThrow(() -> new RuntimeException("Room session not found"));
        if (!"HOST".equals(roomSession.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the host can start the quiz");
        }
        
        roomService.startGame(roomSessionId);
    }
}
