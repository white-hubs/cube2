package com.guhecloud.platform.rocketmq.converter;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.guhecloud.platform.base.standard.jackson.JacksonSerializeModule;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

public class WrappedRocketMQMessageConverter extends RocketMQMessageConverter {

    private final MessageConverter messageConverter;

    private static final boolean JACKSON_PRESENT;

    static {
        ClassLoader classLoader = RocketMQMessageConverter.class.getClassLoader();
        JACKSON_PRESENT =
                ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader) &&
                ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", classLoader);
    }

    public WrappedRocketMQMessageConverter(JwtDecoder jwtDecoder) {
        List<MessageConverter> messageConverters = new ArrayList<>();
        var byteArrayMessageConverter = new ByteArrayMessageConverter();
        byteArrayMessageConverter.setContentTypeResolver(null);
        messageConverters.add(byteArrayMessageConverter);
        messageConverters.add(new StringMessageConverter());
        if (JACKSON_PRESENT) {
            var converter = new MappingJackson2MessageConverter();
            var mapper = converter.getObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.registerModule(new JavaTimeModule());
            mapper.registerModule(new JacksonSerializeModule());
            converter.setObjectMapper(mapper);
            messageConverters.add(converter);
        }
        this.messageConverter = new WrappedMessageConverter(new CompositeMessageConverter(messageConverters), jwtDecoder);
    }

    @Override
    public MessageConverter getMessageConverter() {
        return this.messageConverter;
    }

}
