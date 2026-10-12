package com.guhecloud.cube.gateway.config;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        // @formatter:off
        http.cors(corsSpec -> corsSpec.disable())
                .csrf(csrfSpec -> csrfSpec.disable())
                .authorizeExchange(exchanges->exchanges
        {DEPS_SERVER_PUBLIC}
        .anyExchange()
                .authenticated()
        )
        .oauth2ResourceServer(oauth2ResourceServer ->
                                      oauth2ResourceServer
                                              .authenticationEntryPoint(jwtAuthenticationEntryPoint())
                                              .jwt());
        return http.build();
        // @formatter:on
    }

    public ServerAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new ServerAuthenticationEntryPoint() {
            @Override
            public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                Map<String, Object> responseResult = new HashMap<>();
                responseResult.put("code", HttpStatus.UNAUTHORIZED.value());
                responseResult.put("message", e.getMessage());
                responseResult.put("data", null);
                responseResult.put("timestamp", System.currentTimeMillis());
                try {
                    return response.writeAndFlushWith(Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(objectMapper
                                                                                                                       .writeValueAsString(responseResult)
                                                                                                                       .getBytes(Charset.forName("UTF-8"))))));
                } catch (JsonProcessingException ex) {
                    return Mono.error(ex);
                }
            }
        };
    }

}