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


   

    public void scheduleNextQuestion(String roomId) {


        scheduler.schedule(() -> {
            int questionIndex = gameService.nextQuestionIndex(roomId);
            if (questionIndex == -1) {
                gameService.broadcastEndGame(roomId);
                return;
            }
            gameService.broadcastQuestion(roomId, questionIndex);
        
            scheduler.schedule(() -> {
                gameService.processAnswer(roomId); // first question answers processed
        
                // schedule next question 30s after processing
                scheduler.schedule(() -> scheduleNextQuestion(roomId), 30, TimeUnit.SECONDS);
        
            }, 60, TimeUnit.SECONDS);
        
        }, 0, TimeUnit.SECONDS);


    }
}
