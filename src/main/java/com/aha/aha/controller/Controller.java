package com.aha.aha.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aha.aha.request.JoinRoomRequest;
import com.aha.aha.request.RoomRequest;
import com.aha.aha.response.RoomResponse;
import com.aha.aha.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import jakarta.validation.Valid;


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
    public RoomResponse createRoom(@RequestBody @Valid RoomRequest roomRequest) {
        RoomResponse roomResponse = roomService.createRoom(roomRequest.getHostName(), roomRequest.getTopic(), roomRequest.getMaxPlayers());
        return roomResponse;
    }

    @Operation(summary = "Join a room", description = "Join a room with the given room id and password")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/api/rooms/join")
    public void joinRoom(@RequestBody @Valid JoinRoomRequest joinRoomRequest) {
        roomService.joinRoom(joinRoomRequest.getRoomId(), joinRoomRequest.getPassword(), joinRoomRequest.getPlayerName());
        
    }
}

