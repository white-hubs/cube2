package com.guhecloud.platform.base.standard.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.guhecloud.platform.base.standard.jackson.annotation.Desensitize;
import com.guhecloud.platform.base.util.AssertUtils;
import com.guhecloud.platform.base.util.DesensitizeUtils;
import com.guhecloud.platform.base.util.RegexUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 敏感数据脱敏序列化处理器
 *
 * @author Woody
 * @date 2021/7/12
 */
public class JacksonDesensitizeSerializer extends StdSerializer<String> implements ContextualSerializer {
    /**
     * 敏感数据脱敏表达式/脱敏模型映射表
     */
    private static final Map<String, DesensitizeUtils.Mode> DESENSITIZE_MODE_MAPPING = new ConcurrentHashMap<>();

    /**
     * 数据脱敏模式
     */
    private DesensitizeUtils.Mode mode;

    public JacksonDesensitizeSerializer() {
        super(String.class);
    }

    public JacksonDesensitizeSerializer(DesensitizeUtils.Mode mode) {
        super(String.class);
        this.mode = mode;
    }

    /**
     * 根据脱敏表达式获取敏感数据脱敏模型
     *
     * @param expression 数据脱敏表达式
     *
     * @return 数据脱敏模型
     */
    private DesensitizeUtils.Mode expression2mode(String expression) {
        return DESENSITIZE_MODE_MAPPING.computeIfAbsent(expression, k -> DesensitizeUtils.analyse(expression));
    }

    /**
     * 获取脱敏表达式
     *
     * @param property 对象属性
     *
     * @return 脱敏表达式
     */
    private String getDesensitizeExpression(BeanProperty property) {
        Desensitize desensitize = property.getAnnotation(Desensitize.class);
        if (desensitize == null) {
            desensitize = property.getContextAnnotation(Desensitize.class);
        }
        return desensitize == null ? null : RegexUtils.replace(desensitize.value(), " ", StringUtils.EMPTY_STRING);
    }

    @Override
    public void serialize(String source, JsonGenerator generator, SerializerProvider serializer)
            throws IOException {
        if (this.mode == null || source == null) {
            generator.writeString(source);
        } else {
            generator.writeString(this.mode.desensitize(source));
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializer, BeanProperty property)
            throws JsonMappingException {
        if (property == null) {
            return serializer.getDefaultNullValueSerializer();
        } else if (!String.class.isAssignableFrom(property.getType().getRawClass())) {
            return this;
        }
        String expression = this.getDesensitizeExpression(property);
        AssertUtils.assertTrue(!StringUtils.isEmpty(expression), "Desensitize expression must not be empty");
        return new JacksonDesensitizeSerializer(this.expression2mode(expression));
    }
}
