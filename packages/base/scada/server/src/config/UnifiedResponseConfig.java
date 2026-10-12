package com.guhecloud.kaihe.scada.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.kaihe.scada.server.infrastructure.web.response.UnifiedResponseHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class UnifiedResponseConfig {

    private final RequestMappingHandlerAdapter handlerAdapter;

    private final ObjectMapper objectMapper;

    public UnifiedResponseConfig(RequestMappingHandlerAdapter handlerAdapter, ObjectMapper objectMapper) {
        this.handlerAdapter = handlerAdapter;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void register() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<HandlerMethodReturnValueHandler>();
        returnValueHandlers.add(new UnifiedResponseHandler(objectMapper));
        returnValueHandlers.addAll(handlerAdapter.getReturnValueHandlers());
        handlerAdapter.setReturnValueHandlers(returnValueHandlers);
    }

}
