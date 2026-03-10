package com.aha.aha.request;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;

public class QuestionSetRequest {

    @Valid
    private List<QuestionRequest> questions = new ArrayList<>();
    public List<QuestionRequest> getQuestions() { return questions; }
    public void setQuestions(List<QuestionRequest> questions) { this.questions = questions; }
}
