package com.guhecloud.platform.base.standard.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Jackson序列化处理模块
 * <p>
 * 对JS最大数字支持的处理，JS数字最大安全值为2的53次方
 * <p>
 * Xss过滤
 **/
public class JacksonNullSerializerModifier extends BeanSerializerModifier {
    /**
     * 空数字序列化处理器
     */
    private static final JsonSerializer<Object> NUMBER_NULL_SERIALIZER = new JsonSerializer<Object>() {
        @Override
        public void serialize(Object value, JsonGenerator generator, SerializerProvider provider) throws IOException {
            if (value == null) {
                generator.writeNumber(0);
            } else {
                generator.writeObject(value);
            }
        }
    };

    /**
     * 空字符串序列化处理
     */
    private static final JsonSerializer<Object> STRING_NULL_SERIALIZER = new JsonSerializer<Object>() {
        @Override
        public void serialize(Object value, JsonGenerator generator, SerializerProvider provider) throws IOException {
            if (value == null) {
                generator.writeString("");
            } else {
                generator.writeObject(value);
            }
        }
    };

    /**
     * 空布尔序列化处理
     */
    private static final JsonSerializer<Object> BOOLEAN_NULL_SERIALIZER = new JsonSerializer<Object>() {
        @Override
        public void serialize(Object value, JsonGenerator generator, SerializerProvider provider) throws IOException {
            if (value == null) {
                generator.writeBoolean(false);
            } else {
                generator.writeObject(value);
            }
        }
    };

    /**
     * 空字典序列化处理器
     */
    private static final JsonSerializer<Object> MAP_NULL_SERIALIZER = new JsonSerializer<Object>() {
        @Override
        public void serialize(Object value, JsonGenerator generator, SerializerProvider provider) throws IOException {
            if (value == null) {
                generator.writeStartObject();
                generator.writeEndObject();
            } else {
                generator.writeObject(value);
            }
        }
    };

    /**
     * 空集合序列化处理器
     */
    private static final JsonSerializer<Object> COLLECTION_NULL_SERIALIZER = new JsonSerializer<Object>() {
        @Override
        public void serialize(Object value, JsonGenerator generator, SerializerProvider provider) throws IOException {
            if (value == null) {
                generator.writeStartArray();
                generator.writeEndArray();
            } else {
                generator.writeObject(value);
            }
        }
    };

    private static final JsonSerializer<Object> OTHER_NULL_SERIALIZER = new JsonSerializer<Object>() {
        @Override
        public void serialize(Object value, JsonGenerator generator, SerializerProvider provider) throws IOException {
            if (value == null) {
                generator.writeNull();
            } else {
                generator.writeObject(value);
            }
        }
    };

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription bean,
                                                     List<BeanPropertyWriter> writers
                                                    ) {
        writers.forEach(writer -> {
            Class<?> clazz = writer.getType().getRawClass();
            //            if (Number.class.isAssignableFrom(clazz)) {
            //                writer.assignNullSerializer(NUMBER_NULL_SERIALIZER);
            //            } else
            if (String.class.isAssignableFrom(clazz)) {
                writer.assignNullSerializer(STRING_NULL_SERIALIZER);
            }
            //            else if (Boolean.class.isAssignableFrom(clazz)) {
            //                writer.assignNullSerializer(BOOLEAN_NULL_SERIALIZER);
            //            }
            else if (Map.class.isAssignableFrom(clazz)) {
                writer.assignNullSerializer(MAP_NULL_SERIALIZER);
            } else if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
                writer.assignNullSerializer(COLLECTION_NULL_SERIALIZER);
            } else {
                writer.assignNullSerializer(OTHER_NULL_SERIALIZER);
            }
        });
        return writers;
    }
}
