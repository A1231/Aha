package com.aha.aha.service.websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.aha.aha.entity.Question;
import com.aha.aha.entity.Room;
import com.aha.aha.entity.RoomSession;
import com.aha.aha.entity.User;
import com.aha.aha.repository.QuestionRepository;
import com.aha.aha.repository.RoomRepository;
import com.aha.aha.repository.RoomSessionRepository;
import com.aha.aha.repository.UserRepository;
import com.aha.aha.request.AnswerRequest;
import com.aha.aha.response.websocket.LeaderboardResponse;
import com.aha.aha.response.websocket.QuestionResponse;

@Service
public class GameService {


    private final QuestionRepository questionRepository;
    private final RoomSessionRepository roomSessionRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, String> redisTemplate;
   
    // roomSessionId -> answerRequest (roomSessionId matches WebSocket principal for convertAndSendToUser)
    private final Map<String, AnswerRequest> pendingAnswers = new ConcurrentHashMap<>();

    public GameService(QuestionRepository questionRepository,RoomSessionRepository roomSessionRepository, RoomRepository roomRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate, RedisTemplate<String, String> redisTemplate) {
        this.questionRepository = questionRepository;
        this.roomSessionRepository = roomSessionRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
    }

    public void broadcastQuestion(String roomId, int questionIndex) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        if (!room.isGameStarted()) {
            throw new RuntimeException("Game not started");
        }
        Question question = questionRepository.findById(room.getQuestions().get(questionIndex)).orElseThrow(() -> new RuntimeException("Question not found"));
        QuestionResponse questionResponse = new QuestionResponse(question.getId(), question.getText(), question.getOptions(), questionIndex);
        
        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/question", questionResponse);
    }

  

    public void broadcastEndGame(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        room.setGameStarted(false);
        roomRepository.save(room);
        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/game-ended", "Game ended");
    }

    public int nextQuestionIndex(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        int currentIndex = room.getCurrentQuestionIndex();
        int nextIndex = currentIndex + 1;
        if (nextIndex >= room.getQuestions().size()) {
            nextIndex = -1;

        }
        room.setCurrentQuestionIndex(nextIndex);
        roomRepository.save(room);

        return nextIndex;
    }
    
    public void processAnswer(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        Question question = questionRepository.findById(room.getQuestions().get(room.getCurrentQuestionIndex())).orElseThrow(() -> new RuntimeException("Question not found"));
        
        for (Map.Entry<String, AnswerRequest> entry : pendingAnswers.entrySet()) {
            String roomSessionId = entry.getKey();
            AnswerRequest answerRequest = entry.getValue();
            RoomSession roomSession = roomSessionRepository.findById(roomSessionId).orElseThrow(() -> new RuntimeException("Room session not found"));
            User user = userRepository.findById(roomSession.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            
            if (answerRequest.getAnswerIndex() != question.getCorrectOptionIndex()) {
                System.out.println("INCORRECT");
                messagingTemplate.convertAndSendToUser(roomSessionId, "/queue/room/" + roomId + "/answer", "INCORRECT");

                // update the leaderboard
                redisTemplate.opsForZSet().incrementScore("room:" + roomId + ":leaderboard", user.getId(), 0);
                
            } else {
                System.out.println("CORRECT");
                System.out.println("User previous score: " + user.getScore());
                user.setScore(user.getScore() + 10);
                System.out.println("User new score: " + user.getScore());
                userRepository.save(user);
                messagingTemplate.convertAndSendToUser(roomSessionId, "/queue/room/" + roomId + "/answer", "CORRECT");

                // update the leaderboard
                redisTemplate.opsForZSet().incrementScore("room:" + roomId + ":leaderboard", user.getId(), 10);

                // get the leaderboard
                Set<ZSetOperations.TypedTuple<String>> leaderboard = redisTemplate.opsForZSet().reverseRangeWithScores("room:" + roomId + ":leaderboard", 0, -1);
                List<LeaderboardResponse> leaderboardResponses = new ArrayList<>();
                for (ZSetOperations.TypedTuple<String> tuple : leaderboard) {
                    String playerId = tuple.getValue();
                    User player = userRepository.findById(playerId).orElseThrow(() -> new RuntimeException("User not found"));
                    int score = tuple.getScore().intValue();
                    leaderboardResponses.add(new LeaderboardResponse(player.getId(), player.getName(), score));
                }

                // broadcast the leaderboard
                messagingTemplate.convertAndSend("/topic/room/" + roomId + "/leaderboard", leaderboardResponses);
            }
            pendingAnswers.remove(roomSessionId);
        }
    }

    public void submitAnswer( AnswerRequest answerRequest, String roomSessionId) {
        
        pendingAnswers.put(roomSessionId, answerRequest);
    }

}
