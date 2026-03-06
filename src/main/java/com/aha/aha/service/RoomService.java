package com.aha.aha.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


import org.springframework.stereotype.Service;

import com.aha.aha.entity.Question;
import com.aha.aha.entity.Room;
import com.aha.aha.entity.RoomSession;
import com.aha.aha.entity.User;
import com.aha.aha.repository.QuestionRepository;
import com.aha.aha.repository.RoomRepository;
import com.aha.aha.repository.RoomSessionRepository;
import com.aha.aha.request.QuestionRequest;
import com.aha.aha.response.RoomResponse;
import com.aha.aha.utility.PasswordGenerator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;



@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final PasswordGenerator passwordGenerator;
    private final UserService userService;
    private final QuestionRepository questionRepository;
    private final RoomSessionRepository roomSessionRepository;
    private final RoomEventService roomEventService;

    public RoomService(RoomRepository roomRepository, PasswordGenerator passwordGenerator, 
        UserService userService, QuestionRepository questionRepository, RoomSessionRepository roomSessionRepository, RoomEventService roomEventService) {
        this.roomRepository = roomRepository;
        this.passwordGenerator = passwordGenerator;
        this.userService = userService;
        this.questionRepository = questionRepository;
        this.roomSessionRepository = roomSessionRepository;
        this.roomEventService = roomEventService;
    }

    public RoomResponse createRoom(String hostName, String topic, int maxPlayers, HttpServletResponse response) {

        String roomId = String.valueOf(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        String password = passwordGenerator.generatePassword();

        //create the host user
        User hostUser = userService.createUser(hostName, true, roomId);
        String hostId = hostUser.getId();

        //create the room
        Room room = new Room(roomId, topic, maxPlayers, hostId, LocalDateTime.now(), password);
        Room savedRoom = roomRepository.save(room);
        RoomResponse roomResponse = convertToRoomResponse(savedRoom);

        //create the room session
        RoomSession roomSession = createRoomSession(roomId, hostUser.getId(), "HOST");
       
        // Set the cookie
        setRoomSessionCookie(roomSession.getRoomSessionId(), response);

        return roomResponse;
    }

    public RoomSession createRoomSession(String roomId, String userId, String role) {
        String roomSessionId = String.valueOf(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        Long timeToLive = 86400L; // 24 hours
        RoomSession roomSession = new RoomSession(roomSessionId, roomId, userId, role, timeToLive);
        roomSessionRepository.save(roomSession);
        return roomSession;
    }

    public void setRoomSessionCookie(String roomSessionId, HttpServletResponse response) {
        Cookie cookie = new Cookie("ROOM_SESSION", roomSessionId);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400); // 1 day in seconds
        response.addCookie(cookie);
    }   

    private RoomResponse convertToRoomResponse(Room room) {
        return new RoomResponse(room.getRoomId(), room.getTopic(), room.getMaxPlayers(), room.getPassword(), room.getHostId());
    }

    public User joinRoom(String roomId, String password, String playerName, HttpServletResponse response) {
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

        //create room session
        RoomSession roomSession = createRoomSession(roomId, newPlayer.getId(), "PLAYER");
        setRoomSessionCookie(roomSession.getRoomSessionId(), response);

        //set the cookie
        setRoomSessionCookie(roomSession.getRoomSessionId(), response);

        //broadcast the player joined event
        roomEventService.broadcastPlayerJoined(roomId, newPlayer.getName());

        return newPlayer;
    }

    public void addQuestionsToRoom(List<QuestionRequest> questions) {
        Room room = roomRepository.findById(questions.get(0).getRoomId()).orElseThrow(() -> new RuntimeException("Room not found"));
        
        for (QuestionRequest question : questions) {
            String questionId = String.valueOf(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
            Question newQuestion = new Question(questionId, question.getText(), question.getOptions(), question.getCorrectOptionIndex());
            Question savedQuestion = questionRepository.save(newQuestion);
            room.getQuestions().add(newQuestion.getId());
        }
        roomRepository.save(room);
    }

    public void startGame(String roomSessionId) {
        RoomSession roomSession = roomSessionRepository.findByRoomSessionId(roomSessionId).orElseThrow(() -> new RuntimeException("Room session not found"));
        Room room = roomRepository.findById(roomSession.getRoomId()).orElseThrow(() -> new RuntimeException("Room not found"));
        if (room.isGameStarted()) {
            throw new RuntimeException("Game already started");
        }
        room.setGameStarted(true);
        roomRepository.save(room);

        //broadcast the game started event
        roomEventService.broadcastGameStarted(roomSession.getRoomId(), "Game started");
    }
}
