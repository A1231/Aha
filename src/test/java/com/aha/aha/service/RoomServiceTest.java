package com.aha.aha.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import com.aha.aha.entity.Question;
import com.aha.aha.entity.Room;
import com.aha.aha.entity.RoomSession;
import com.aha.aha.entity.User;
import com.aha.aha.exception.BadRequestException;
import com.aha.aha.exception.ResourceNotFoundException;
import com.aha.aha.repository.QuestionRepository;
import com.aha.aha.repository.RoomRepository;
import com.aha.aha.repository.RoomSessionRepository;
import com.aha.aha.request.QuestionRequest;
import com.aha.aha.request.RoomRequest;
import com.aha.aha.response.RoomResponse;
import com.aha.aha.service.websocket.GameService;
import com.aha.aha.service.websocket.RoomEventService;
import com.aha.aha.utility.PasswordGenerator;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @InjectMocks
    RoomService roomService;

    @Mock
    RoomRepository roomRepository;

    @Mock
    UserService userService;

    @Mock
    PasswordGenerator passwordGenerator;

    @Mock
    RoomSessionRepository roomSessionRepository;

    @Mock
    RoomEventService roomEventService;

    @Mock
    QuestionRepository questionRepository;

    @Test
    public void testShouldCreateRoomAndUserAndRoomSession() {
       
        String hostName = "testHost";
        String topic = "testTopic";
        int maxPlayers = 10;
        MockHttpServletResponse response = new MockHttpServletResponse();

        Mockito.when(passwordGenerator.generatePassword()).thenReturn("testPassword");
        Mockito.when(userService.createUser(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(new User("testUserId", hostName, 0, true, "testRoomId"));
        Mockito.when(roomRepository.save(Mockito.any(Room.class))).thenReturn(new Room("testRoomId", topic, maxPlayers, "testHostId", LocalDateTime.now(), "testPassword"));
        Mockito.when(roomSessionRepository.save(Mockito.any(RoomSession.class))).thenReturn(new RoomSession("testRoomSessionId", "testRoomId", "testUserId", "HOST", 7200L));

        RoomResponse roomResponse = roomService.createRoom(hostName, topic, maxPlayers, response);

        //Assert
        Assertions.assertNotNull(roomResponse);
        Assertions.assertEquals("testRoomId", roomResponse.getRoomId());
        Assertions.assertEquals(topic, roomResponse.getTopic());
        Assertions.assertEquals(maxPlayers, roomResponse.getMaxPlayers());
        Assertions.assertEquals("testPassword", roomResponse.getPassword());
        Assertions.assertEquals("testHostId", roomResponse.getHostId());
    }


    @Test
    public void createRoomSessionShouldCreateRoomSession() {
        String roomId = "testRoomId";
        String userId = "testUserId";
        String role = "HOST";

        RoomSession roomSession = roomService.createRoomSession(roomId, userId, role);

        //Assert
        Assertions.assertNotNull(roomSession);
        Assertions.assertEquals(roomId, roomSession.getRoomId());
        Assertions.assertEquals(userId, roomSession.getUserId());
        Assertions.assertEquals(role, roomSession.getRole());
        Assertions.assertEquals(7200L, roomSession.getTimeToLive());
    }

    @Test
    public void joinRoomShouldJoinRoom() {
        String roomId = "testRoomId";
        String password = "testPassword";
        String playerName = "testPlayer";
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        Mockito.when(roomRepository.findById(roomId)).thenReturn(Optional.of(new Room("testRoomId", "testTopic", 10, "testHostId", LocalDateTime.now(), "testPassword")));
        Mockito.when(userService.createUser(playerName, false, roomId)).thenReturn(new User("testUserId", playerName, 0, false, roomId));
        Mockito.when(roomRepository.save(Mockito.any(Room.class))).thenReturn(new Room("testRoomId", "testTopic", 10, "testHostId", LocalDateTime.now(), "testPassword"));
        Mockito.when(roomSessionRepository.save(Mockito.any(RoomSession.class))).thenReturn(new RoomSession("testRoomSessionId", "testRoomId", "testUserId", "PLAYER", 7200L));
       // Mockito.when(roomEventService.broadcastPlayerJoined(roomId, Arrays.asList(playerName))).thenReturn(null);

        RoomResponse roomResponse = roomService.joinRoom(roomId, password, playerName, response);

        //Assert
        Assertions.assertNotNull(roomResponse);
        Assertions.assertEquals(roomId, roomResponse.getRoomId());
        Assertions.assertEquals("testTopic", roomResponse.getTopic());
        Assertions.assertEquals(10, roomResponse.getMaxPlayers());
        Assertions.assertEquals("testPassword", roomResponse.getPassword());
        Assertions.assertEquals("testHostId", roomResponse.getHostId());
       

    }

    @Test
    public void joinRoomShouldThrowExceptionIfRoomNotFound() {
        String roomId = "testRoomId";
        String password = "testPassword";
        String playerName = "testPlayer";
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        Mockito.when(roomRepository.findById(roomId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> roomService.joinRoom(roomId, password, playerName, response));
        Assertions.assertEquals("Room not found", exception.getMessage());
    }

    @Test
    public void joinRoomShouldThrowExceptionIfPasswordIsIncorrect() {
        String roomId = "testRoomId";
        String password = "incorrectPassword";
        String playerName = "testPlayer";
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        Mockito.when(roomRepository.findById(roomId)).thenReturn(Optional.of(new Room("testRoomId", "testTopic", 10, "testHostId", LocalDateTime.now(), "testPassword")));
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> roomService.joinRoom(roomId, password, playerName, response));
        Assertions.assertEquals("Invalid password", exception.getMessage());
    }



    @Test
    public void addQuestionsToRoomShouldAddQuestionsToRoom() {
        String roomId = "testRoomId";
        Room room = new Room("testRoomId", "testTopic", 10, "testHostId", LocalDateTime.now(), "testPassword");
        List<QuestionRequest> questions = new ArrayList<>();
        List<String> options = new ArrayList<>();
        options.add("testOption1");
        options.add("testOption2");
        options.add("testOption3");
        options.add("testOption4");
        questions.add(new QuestionRequest("testQuestion1", options, 0));
        questions.add(new QuestionRequest("testQuestion2", options, 1));
        questions.add(new QuestionRequest("testQuestion3", options, 2));
        questions.add(new QuestionRequest("testQuestion4", options, 3));
        roomService.addQuestionsToRoom(questions, room);

        
        Mockito.verify(questionRepository, Mockito.times(4)).save(Mockito.any(Question.class));
    }


}
