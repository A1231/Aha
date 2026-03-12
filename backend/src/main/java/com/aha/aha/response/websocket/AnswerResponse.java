package com.aha.aha.response.websocket;

public class AnswerResponse {
    private int correctAnswerIndex;
    private boolean isCorrect;
    
    public AnswerResponse(int correctAnswerIndex, boolean isCorrect) {
        this.correctAnswerIndex = correctAnswerIndex;
        this.isCorrect = isCorrect;
    }

    public AnswerResponse() {}
    
    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
    
    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
