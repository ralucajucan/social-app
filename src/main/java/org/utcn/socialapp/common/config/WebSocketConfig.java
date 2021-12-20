package org.utcn.socialapp.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.utcn.socialapp.auth.jwt.JWTUtility;
import org.utcn.socialapp.user.User;
import org.utcn.socialapp.user.UserService;

//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry
//                .addEndpoint("/api/message")
//                .setAllowedOriginPatterns("*");
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry
//                .setApplicationDestinationPrefixes("/ws")
//                .enableSimpleBroker("/topic");
//    }
//}

@Configuration
@EnableAsync
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JWTUtility jwtUtility;
    private final UserService userService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // example: '/api/message'?
        config.enableSimpleBroker("/topic","/queue");
        config.setApplicationDestinationPrefixes("/api");
//        config.setUserDestinationPrefix("/user/");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // -> ws://localhost:8080
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins("*");
//        registry
//                .addEndpoint("/ws")
//                .setAllowedOrigins("*") //http://localhost:4200
//                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authorization = accessor.getFirstNativeHeader("Authorization");
                    if (StringUtils.hasLength(authorization)) {
                        String token = authorization.substring(7);
                        String userEmail = jwtUtility.getEmailFromToken(token);
                        if (SecurityContextHolder.getContext().getAuthentication() == null) {
                            User user = userService.loadUserByUsername(userEmail);
                            if (jwtUtility.validateToken(token, user)) {
                                UsernamePasswordAuthenticationToken authenticationToken =
                                        new UsernamePasswordAuthenticationToken(
                                                user,
                                                null,
                                                user.getAuthorities()
                                        );

                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                                accessor.setUser(authenticationToken);
                            }
                        }
                    }
                }
                return message;
            }
        });
    }
}
