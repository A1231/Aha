package com.aha.aha.request;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class QuestionRequest {

    @NotEmpty(message = "Room id is required")
    private String roomId;

    @NotEmpty(message = "Text is required")
    private String text;

    @NotEmpty(message = "Options are required")
    @Size(min = 4, max = 4, message = "Options must be exactly 4")
    private List<String> options = new ArrayList<>(); 

    @NotNull(message = "Correct option index is required")
    @Min(value = 0, message = "Correct option index must be 0-based")
    @Max(value = 3, message = "Correct option index must be 3-based")
    private int correctOptionIndex; // 0-based

    public QuestionRequest(String roomId, String text, List<String> options, int correctOptionIndex) {
        this.roomId = roomId;
        this.text = text;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    // getters & setters
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(int correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }
}