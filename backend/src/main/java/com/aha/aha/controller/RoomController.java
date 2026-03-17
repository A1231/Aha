package com.aha.aha.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aha.aha.entity.Room;
import com.aha.aha.entity.RoomSession;
import com.aha.aha.exception.ForbiddenException;
import com.aha.aha.exception.ResourceNotFoundException;
import com.aha.aha.repository.RoomRepository;
import com.aha.aha.repository.RoomSessionRepository;
import com.aha.aha.request.JoinRoomRequest;
import com.aha.aha.request.QuestionSetRequest;
import com.aha.aha.request.RoomRequest;
import com.aha.aha.response.HealthResponse;
import com.aha.aha.response.RoomResponse;
import com.aha.aha.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;



@Tag(name="Room REST API Endpoints", description = "Operations related to info about rooms")
@RequestMapping("/api")
@RestController
public class RoomController {

    
    private final RoomService roomService;
    private final RoomSessionRepository roomSessionRepository;
    private final RoomRepository roomRepository;

    public RoomController(RoomService roomService, RoomSessionRepository roomSessionRepository, RoomRepository roomRepository) {
        this.roomService = roomService;
        this.roomSessionRepository = roomSessionRepository;
        this.roomRepository = roomRepository;
    }


    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public HealthResponse health() {
        return new HealthResponse("healthy");
    }


    @Operation(summary = "Create a new room", description = "Create a new room with the given host name, topic, and max players")
    @PostMapping("/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse createRoom( @RequestBody @Valid RoomRequest roomRequest, HttpServletResponse response) {
        RoomResponse roomResponse = roomService.createRoom(roomRequest.getHostName(), roomRequest.getTopic(), roomRequest.getMaxPlayers(), response);

        
        return roomResponse;
    }

    @Operation(summary = "Join a room", description = "Join a room with the given room id and password")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rooms/join")
    public RoomResponse joinRoom(@RequestBody @Valid JoinRoomRequest joinRoomRequest, HttpServletResponse response) {
        return roomService.joinRoom(joinRoomRequest.getRoomId(), joinRoomRequest.getPassword(), joinRoomRequest.getPlayerName(), response);
    }

    @Operation(summary = "Add questions to a room", description = "Add a question to a room with the given room id and question. Only the host can add questions.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/rooms/questions")
    public void addQuestionsToRoom(@CookieValue("ROOM_SESSION") String roomSessionId, @RequestBody @Valid QuestionSetRequest questionSetRequest) {
        RoomSession roomSession = roomSessionRepository.findByRoomSessionId(roomSessionId).orElseThrow(() -> new ResourceNotFoundException("Room session not found"));
        Room room = roomRepository.findById(roomSession.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        if (!"HOST".equals(roomSession.getRole())) {
            throw new ForbiddenException("Only the host can add questions");
        }
        roomService.addQuestionsToRoom(questionSetRequest.getQuestions(), room);
    }
    
    @Operation(summary = "Start the game", description = "Start the game for the given room id")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rooms/start")
    public void startQuiz(@CookieValue("ROOM_SESSION") String roomSessionId) {
        RoomSession roomSession = roomSessionRepository.findByRoomSessionId(roomSessionId).orElseThrow(() -> new ResourceNotFoundException("Room session not found"));
        if (!"HOST".equals(roomSession.getRole())) {
            throw new ForbiddenException("Only the host can start the quiz");
        }
        
        roomService.startGame(roomSessionId);
    }

    @Operation(summary = "End the game", description = "End the game for the given room id")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rooms/end")
    public void endQuiz(@CookieValue("ROOM_SESSION") String roomSessionId) {
        RoomSession roomSession = roomSessionRepository.findByRoomSessionId(roomSessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Room session not found"));
        if (!"HOST".equals(roomSession.getRole())) {
            throw new ForbiddenException("Only the host can end the quiz");
        }

        roomService.endGame(roomSessionId);
    }
}
