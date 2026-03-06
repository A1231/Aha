package com.aha.aha.config.websocket;

import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.aha.aha.entity.RoomSession;
import com.aha.aha.repository.RoomSessionRepository;

/**
 * Validates ROOM_SESSION (from handshake cookie) on CONNECT/SUBSCRIBE/SEND.
 * For SUBSCRIBE, ensures the destination room matches the session's room.
 */
@Component
public class RoomSessionChannelInterceptor implements ChannelInterceptor {

    private static final String ROOM_TOPIC_PREFIX = "/topic/room/";

    private final RoomSessionRepository roomSessionRepository;

    public RoomSessionChannelInterceptor(RoomSessionRepository roomSessionRepository) {
        this.roomSessionRepository = roomSessionRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (!StompCommand.CONNECT.equals(accessor.getCommand())
                && !StompCommand.SUBSCRIBE.equals(accessor.getCommand())
                && !StompCommand.SEND.equals(accessor.getCommand())) {
            return message;
        }

        Map<String, Object> sessionAttrs = accessor.getSessionAttributes();
        if (sessionAttrs == null || !sessionAttrs.containsKey(RoomSessionHandshakeInterceptor.ROOM_SESSION_ATTR)) {
            throw new IllegalArgumentException("Missing room session token");
        }

        String roomSessionId = (String) sessionAttrs.get(RoomSessionHandshakeInterceptor.ROOM_SESSION_ATTR);
        RoomSession session = roomSessionRepository.findById(roomSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired room session token"));

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            if (destination != null && destination.startsWith(ROOM_TOPIC_PREFIX)) {
                String subscribedRoomId = destination.substring(ROOM_TOPIC_PREFIX.length());
                if (!session.getRoomId().equals(subscribedRoomId)) {
                    throw new IllegalArgumentException("Session does not match room subscription");
                }
            }
        }

        if (StompCommand.SEND.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            if (destination != null && destination.startsWith("/app/room/")) {
                String roomId = extractRoomId(destination);
        
                if (!session.getRoomId().equals(roomId)) {
                    throw new IllegalArgumentException("Invalid room action");
                }
            }
        }

        return message;
    }

    private String extractRoomId(String destination) {
        return destination.substring(destination.lastIndexOf('/') + 1);
    }
}
