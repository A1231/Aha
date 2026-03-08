package com.aha.aha.config.websocket;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * Sets the UserPrincipal at the WebSocket session level during handshake,
 * so that SimpUserRegistry can resolve it for convertAndSendToUser().
 * The ROOM_SESSION attribute is placed into the attributes map by
 * RoomSessionHandshakeInterceptor (which runs before this handler).
 * RoomSessionHandshakeHandler is called during the WebSocket handshake phase, specifically after the handshake interceptors run but before the WebSocket session is fully established.
 * 
 */
public class RoomSessionHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        String roomSessionId = (String) attributes.get(RoomSessionHandshakeInterceptor.ROOM_SESSION_ATTR);
        if (roomSessionId != null) {
            return new UserPrincipal(roomSessionId);
        }
        return super.determineUser(request, wsHandler, attributes);
    }
}
