package com.aha.aha.response.websocket;

public class LeaderboardResponse {
    
    private String playerId;
    private String playerName;
    private int score;

    public LeaderboardResponse(String playerId, String playerName, int score) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }   

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }   
    
    public void setScore(int score) {
        this.score = score;
    }   
}
