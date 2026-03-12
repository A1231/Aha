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
        config.enableSimpleBroker("/topic", "/queue"); // for broadcasting events
        config.setApplicationDestinationPrefixes("/app"); // client sends messages here
        config.setUserDestinationPrefix("/user"); // user destination prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-aha")
                .setHandshakeHandler(new RoomSessionHandshakeHandler())
                .setAllowedOriginPatterns("http://localhost:5173", "http://localhost:8080")
                .addInterceptors(roomSessionHandshakeInterceptor)
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(roomSessionChannelInterceptor);
    }
}
