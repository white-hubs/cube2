package com.guhecloud.platform.base.standard.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.NonNull;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Jackson小数序列化处理器
 *
 * @author Woody
 * @date 2021/7/12
 */
public class JacksonDecimalSerializer extends StdSerializer<Number> implements ContextualSerializer {
    /**
     * 默认小数保留位数
     */
    public static final int DEFAULT_SCALE = 2;

    /**
     * 小数格式化注解实例
     */
    private Decimal decimal;

    public JacksonDecimalSerializer() {
        super(Number.class);
    }

    public JacksonDecimalSerializer(@NonNull Decimal decimal) {
        super(Number.class);
        this.decimal = decimal;
    }

    /**
     * 判断是否是小数属性
     *
     * @param property 属性对象
     *
     * @return true/false
     */
    private boolean isDecimalProperty(BeanProperty property) {
        return Float.class.isAssignableFrom(property.getType().getRawClass())
               || Double.class.isAssignableFrom(property.getType().getRawClass())
               || BigDecimal.class.isAssignableFrom(property.getType().getRawClass());
    }

    /**
     * 获取属性小数格式注解实例
     *
     * @param property 属性对象
     *
     * @return 小数格式化注解实例
     */
    private Decimal getPropertyDecimal(BeanProperty property) {
        Decimal format = property.getAnnotation(Decimal.class);
        if (format == null) {
            format = property.getContextAnnotation(Decimal.class);
        }
        return format;
    }

    @Override
    public void serialize(Number number, JsonGenerator generator, SerializerProvider serializer)
            throws IOException {
        if (this.decimal == null || number == null) {
            generator.writeObject(number);
        } else {
            BigDecimal decimal = number instanceof BigDecimal ? (BigDecimal) number : new BigDecimal(number.toString());
            generator.writeString(decimal.setScale(this.decimal.scale(), this.decimal.mode()).toPlainString());
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializer, BeanProperty property)
            throws JsonMappingException {
        if (property == null) {
            return serializer.getDefaultNullValueSerializer();
        } else if (!this.isDecimalProperty(property)) {
            return this;
        }
        Decimal decimal = this.getPropertyDecimal(property);
        return decimal == null ? this : new JacksonDecimalSerializer(decimal);
    }
}
