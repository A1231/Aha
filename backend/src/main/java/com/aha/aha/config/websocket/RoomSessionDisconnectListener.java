package com.aha.aha.config.websocket;

import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.aha.aha.repository.RoomSessionRepository;

/**
 * When the browser is closed or the client disconnects from the WebSocket,
 * removes the corresponding room session from Redis so the token cannot be reused
 * and sessions do not linger until TTL.
 */
@Component
public class RoomSessionDisconnectListener {

    private final RoomSessionRepository roomSessionRepository;

    public RoomSessionDisconnectListener(RoomSessionRepository roomSessionRepository) {
        this.roomSessionRepository = roomSessionRepository;
    }

    @EventListener
    public void onSessionDisconnect(SessionDisconnectEvent event) {
        Map<String, Object> sessionAttrs = SimpMessageHeaderAccessor.getSessionAttributes(
                event.getMessage().getHeaders());
        if (sessionAttrs == null) {
            return;
        }

        String roomSessionId = (String) sessionAttrs.get(RoomSessionHandshakeInterceptor.ROOM_SESSION_ATTR);
        //System.out.println("Deleting session: " + roomSessionId);
        if (roomSessionId != null) {
            roomSessionRepository.deleteById(roomSessionId);
        }
    }
}
