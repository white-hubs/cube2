package com.guhecloud.cube.gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebExceptionConfig {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    @Primary
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler() {
        return (exchange, throwable) -> {
            ServerHttpResponse response = exchange.getResponse();
            if (response.isCommitted()) {
                return Mono.error(throwable);
            }
            if (throwable instanceof ResponseStatusException) {
                response.setStatusCode(((ResponseStatusException) throwable).getStatusCode());
            } else {
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> responseResult = new HashMap<>();
            responseResult.put("code", response.getStatusCode().value());
            responseResult.put("message", throwable.getMessage());
            responseResult.put("timestamp", System.currentTimeMillis());
            try {
                return response.writeAndFlushWith(Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(objectMapper
                                                                                                                   .writeValueAsString(responseResult)
                                                                                                                   .getBytes(Charset.forName("UTF-8"))))));
            } catch (JsonProcessingException ex) {
                return Mono.error(ex);
            }
        };
    }

}
