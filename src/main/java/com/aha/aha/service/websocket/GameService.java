package com.aha.aha.service.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.aha.aha.entity.Question;
import com.aha.aha.entity.Room;
import com.aha.aha.repository.QuestionRepository;
import com.aha.aha.repository.RoomRepository;
import com.aha.aha.repository.RoomSessionRepository;
import com.aha.aha.response.websocket.QuestionResponse;

@Service
public class GameService {


    private final QuestionRepository questionRepository;
    private final RoomSessionRepository roomSessionRepository;
    private final RoomRepository roomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // Track current question per room
    private final Map<String, Integer> roomCurrentQuestionIndex = new ConcurrentHashMap<>();

    public GameService(QuestionRepository questionRepository,RoomSessionRepository roomSessionRepository, RoomRepository roomRepository, SimpMessagingTemplate messagingTemplate) {
        this.questionRepository = questionRepository;
        this.roomSessionRepository = roomSessionRepository;
        this.roomRepository = roomRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastQuestion(String roomId, int questionIndex) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        if (!room.isGameStarted()) {
            throw new RuntimeException("Game not started");
        }
        Question question = questionRepository.findById(room.getQuestions().get(questionIndex)).orElseThrow(() -> new RuntimeException("Question not found"));
        QuestionResponse questionResponse = new QuestionResponse(question.getId(), question.getText(), question.getOptions());
        
        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/question", questionResponse);
    }


}
