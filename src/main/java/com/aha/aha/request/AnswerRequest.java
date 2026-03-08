package com.aha.aha.request;

public class AnswerRequest {
    private int answerIndex;
    private int questionIndex;

    public AnswerRequest() {}

    public AnswerRequest(int answerIndex, int questionIndex) {
        this.answerIndex = answerIndex;
        this.questionIndex = questionIndex;
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }
    
    public void setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }
}
