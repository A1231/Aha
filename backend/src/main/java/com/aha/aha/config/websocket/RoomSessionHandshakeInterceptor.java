package com.aha.aha.config.websocket;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.WebUtils;

import com.aha.aha.repository.RoomSessionRepository;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Copies the ROOM_SESSION cookie from the WebSocket handshake request into the
 * WebSocket session attributes so it can be read in STOMP handlers and interceptors.
 */
@Component
public class RoomSessionHandshakeInterceptor implements HandshakeInterceptor {

    public static final String ROOM_SESSION_ATTR = "ROOM_SESSION";
    private final RoomSessionRepository roomSessionRepository;

    public RoomSessionHandshakeInterceptor(RoomSessionRepository roomSessionRepository) {
        this.roomSessionRepository = roomSessionRepository;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        
        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            return false;
        }
    
        HttpServletRequest req = servletRequest.getServletRequest();
        Cookie cookie = WebUtils.getCookie(req, "ROOM_SESSION");
    
        if (cookie == null) {
            return false;
        }
    
        String sessionId = cookie.getValue();
    
        // Validate with Redis
        if (!roomSessionRepository.existsById(sessionId)) {
            return false;
        }
    
        attributes.put(ROOM_SESSION_ATTR, sessionId);
    
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception ex) {
        // no-op
    }
}
