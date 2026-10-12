package com.guhecloud.platform.base.standard.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Date;

public final class ObjectMapperConfig {

    public static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Date.class, new DateSerializer());
        javaTimeModule.addDeserializer(Date.class, new DateDeserializers.DateDeserializer());
        objectMapper.registerModule(javaTimeModule);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 允许单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        SerializerFactory factory = objectMapper.getSerializerFactory();
        objectMapper.setSerializerFactory(factory.withSerializerModifier(new JacksonNullSerializerModifier()));
        objectMapper.registerModule(new JacksonSerializeModule());
        // 如果遇到未知属性时,不抛出异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 允许反序列化key值不带引号的情况
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        // 忽略无法转换的对象
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.getFactory().setStreamWriteConstraints(StreamWriteConstraints.builder()
                                                                    .maxNestingDepth(2000)
                                                                    .build()
                                                           );
        objectMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, true);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
}
