package com.aha.aha.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value= "question", timeToLive = 86400) // 24 hours
public class Question {
    @Id
    private String id;
    private String text;
    private List<String> options;
    private int correctOptionIndex;
    

    public Question() {
        options = new ArrayList<>();
    }

    public Question(String id, String text, List<String> options, int correctOptionIndex) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }   
}
