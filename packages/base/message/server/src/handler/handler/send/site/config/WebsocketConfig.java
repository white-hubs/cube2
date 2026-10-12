package com.guhecloud.cube.base.message2.server.handler.handler.send.site.config;

import com.guhecloud.cube.base.message2.server.handler.handler.send.site.handler.MsgWebsocketHandler;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.interceptor.JwtHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(msgWebsocketHandler(), "/websocket/msg", "/websocket/msg/").addInterceptors(jwtHandshakeInterceptor())
                .setAllowedOrigins("*");
    }

    @Bean("messageWebSocketHandler")
    public MsgWebsocketHandler msgWebsocketHandler() {
        return new MsgWebsocketHandler(this.redisTemplate);
    }

    @Bean
    public HandshakeInterceptor jwtHandshakeInterceptor() {
        return new JwtHandshakeInterceptor();
    }

}
