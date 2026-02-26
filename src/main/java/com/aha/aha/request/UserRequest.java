package com.aha.aha.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserRequest {
    
    @NotEmpty(message = "Name is required")
    private String name;

    @NotNull(message = "Is host is required")
    private boolean isHost;

    @NotEmpty(message = "Room id is required")
    private String roomId;

    public String getName() {
        return name;
    }
    
    public boolean isHost() {
        return isHost;
    }
}
