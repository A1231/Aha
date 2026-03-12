package com.aha.aha.config.websocket;

import java.security.Principal;

public class UserPrincipal implements Principal {
    private String sessionToken;

    public UserPrincipal(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    @Override
    public String getName() {
        return sessionToken;
    }
    
}