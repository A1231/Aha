package com.aha.aha.response.websocket;

import java.util.List;

public class QuestionResponse {

    private String questionId;
    private String questionText;
    private List<String> options;
    private int questionIndex;

    public QuestionResponse(String questionId, String questionText, List<String> options) {
        this(questionId, questionText, options, -1);
    }

    public QuestionResponse(String questionId, String questionText, List<String> options, int questionIndex) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.options = options;
        this.questionIndex = questionIndex;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
