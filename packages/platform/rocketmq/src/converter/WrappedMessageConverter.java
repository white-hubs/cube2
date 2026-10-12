package com.guhecloud.platform.rocketmq.converter;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WrappedMessageConverter implements MessageConverter {

    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$", Pattern.CASE_INSENSITIVE);

    private final MessageConverter messageConverter;
    private final JwtDecoder jwtDecoder;

    public WrappedMessageConverter(@NonNull MessageConverter messageConverter, JwtDecoder jwtDecoder) {
        this.messageConverter = messageConverter;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Object fromMessage(Message<?> message, Class<?> targetClass) {

        // 第一次转换，统一按照包装器的类型进行转化还原
        MessageWrapper wrapper = (MessageWrapper) this.messageConverter.fromMessage(message, MessageWrapper.class);
        if (wrapper == null) {
            return null;
        }

        // 从包装器提取 Requester 并设置到上下文，作用类似于http请求时 WebRequestContextListener 的功能
        this.extractRequester(wrapper.getRequester());
        // 从包装器提取 Principal 并设置到上下文
        this.extractPrincipal(wrapper.getRequester());

        // 包装器内实际消息负载的获取及转换
        Object payload = wrapper.getPayload();
        if (payload == null || (payload instanceof String && targetClass == String.class)) {
            return payload;
        }
        return this.messageConverter.fromMessage(new GenericMessage<>(payload), targetClass);
    }

    @Override
    public Message<?> toMessage(Object payload, MessageHeaders headers) {
        MessageWrapper messageWrapper = MessageWrapper.builder().payload(payload).requester(SessionContextHolder.getRequester(true))
                .build();
        return this.messageConverter.toMessage(messageWrapper, headers);
    }

    private void extractRequester(Requester requester) {
        if (requester != null) {
            SessionContextHolder.setRequester(requester);
        }
    }

    private void extractPrincipal(Requester requester) {
        if (requester == null || StringUtils.isEmpty(requester.getAuthorization())) {
            return;
        }
        String authorization = requester.getAuthorization();
        if (StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
            Matcher matcher = authorizationPattern.matcher(authorization);
            if (!matcher.matches()) {
                return;
            }
            String authorizationToken = matcher.group("token");
            // 此处触发 decode 即可，不需处理解码出来的对象，因为 jwtDecoder 会自动调用一系列 validators，包括
            // JwtPrincipalValidator，即提取 Principal 并设置到上下文
            this.jwtDecoder.decode(authorizationToken);
        }
    }

}
