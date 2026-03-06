package com.aha.aha.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final RoomSessionHandshakeInterceptor roomSessionHandshakeInterceptor;
    private final RoomSessionChannelInterceptor roomSessionChannelInterceptor;

    public WebSocketConfig(RoomSessionHandshakeInterceptor roomSessionHandshakeInterceptor,
                           RoomSessionChannelInterceptor roomSessionChannelInterceptor) {
        this.roomSessionHandshakeInterceptor = roomSessionHandshakeInterceptor;
        this.roomSessionChannelInterceptor = roomSessionChannelInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // for broadcasting events
        config.setApplicationDestinationPrefixes("/app"); // client sends messages here
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-aha") // websocket handshake endpoint
                .setAllowedOriginPatterns("http://localhost:8080") // allow all origins
                .addInterceptors(roomSessionHandshakeInterceptor)
                .withSockJS(); // fallback for browsers without WS support
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(roomSessionChannelInterceptor);
    }
}
