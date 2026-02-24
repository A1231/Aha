package com.aha.aha.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aha.aha.entity.Room;
import com.aha.aha.entity.User;
import com.aha.aha.repository.RoomRepository;
import com.aha.aha.response.RoomResponse;
import com.aha.aha.utility.PasswordGenerator;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final PasswordGenerator passwordGenerator;
    private final UserService userService;

    public RoomService(RoomRepository roomRepository, PasswordGenerator passwordGenerator, UserService userService) {
        this.roomRepository = roomRepository;
        this.passwordGenerator = passwordGenerator;
        this.userService = userService;
    }

    public RoomResponse createRoom(String hostName, String topic, int maxPlayers) {

        String roomId = String.valueOf(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        String password = passwordGenerator.generatePassword();

        //create the host user
        User hostUser = userService.createUser(hostName, true, roomId);
        String hostId = hostUser.getId();

        //create the room
        Room room = new Room(roomId, topic, maxPlayers, hostId, LocalDateTime.now(), password);
        Room savedRoom = roomRepository.save(room);
        RoomResponse roomResponse = convertToRoomResponse(savedRoom);
        return roomResponse;
    }

    private RoomResponse convertToRoomResponse(Room room) {
        return new RoomResponse(room.getRoomId(), room.getTopic(), room.getMaxPlayers(), room.getPassword(), room.getHostId());
    }

    public void joinRoom(String roomId, String password, String playerName) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        if (!room.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        if (room.getPlayers() != null && room.getPlayers().size() >= room.getMaxPlayers()) {
            throw new RuntimeException("Room is full");
        }

        //create player
        User newPlayer = userService.createUser(playerName, false, roomId);
        room.getPlayers().add(newPlayer.getId());
        roomRepository.save(room);
        
    }
}
