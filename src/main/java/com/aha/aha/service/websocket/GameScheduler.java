package com.aha.aha.service.websocket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class GameScheduler {
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final GameService gameService;

    public GameScheduler(GameService gameService) {
        this.gameService = gameService;
    }


   

    public void scheduleNextQuestion(String roomId, int nextQuestionIndex, long delaySeconds, int totalQuestions) {
            
        if (nextQuestionIndex >= totalQuestions) {
            return; // stop recursion
        }
            scheduler.schedule(() -> {
                gameService.broadcastQuestion(roomId, nextQuestionIndex);
                scheduleNextQuestion(roomId, nextQuestionIndex+1, delaySeconds, totalQuestions);
            }, delaySeconds, TimeUnit.SECONDS);
    }
}
