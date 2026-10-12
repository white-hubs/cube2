package com.guhecloud.platform.base.standard.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class JacksonSerializeModule extends SimpleModule {
    /**
     * JS最大整型数（2的53次方）
     */
    private static final BigInteger JS_MAX_INTEGER_VALUE = BigInteger.valueOf(2).pow(53);

    /**
     * JS最大小数（2的53次方）
     */
    private static final BigDecimal JS_MAX_DECIMAL_VALUE = BigDecimal.valueOf(2).pow(53);

    {
        // 设置整数序列化处理器
        this.addSerializer(new JsonSerializer<Long>() {
            @Override
            public Class<Long> handledType() {
                return Long.class;
            }

            @Override
            public void serialize(Long number, JsonGenerator generator, SerializerProvider serializer)
                    throws IOException {
                if (isSafelyNumber(number)) {
                    generator.writeNumber(number);
                } else {
                    generator.writeString(number.toString());
                }
            }
        });

        // 设置浮点数序列化处理器
        this.addSerializer(new JsonSerializer<Double>() {
            @Override
            public Class<Double> handledType() {
                return Double.class;
            }

            @Override
            public void serialize(Double number, JsonGenerator generator, SerializerProvider serializer)
                    throws IOException {
                if (isSafelyNumber(number)) {
                    generator.writeNumber(number);
                } else {
                    generator.writeString(number.toString());
                }
            }
        });

        // 设置大整数序列化处理器
        this.addSerializer(new JsonSerializer<BigInteger>() {
            @Override
            public Class<BigInteger> handledType() {
                return BigInteger.class;
            }

            @Override
            public void serialize(BigInteger number, JsonGenerator generator, SerializerProvider serializer)
                    throws IOException {
                if (isSafelyNumber(number)) {
                    generator.writeNumber(number);
                } else {
                    generator.writeString(number.toString());
                }
            }
        });

        // 设置大浮点数序列化处理器
        this.addSerializer(new JsonSerializer<BigDecimal>() {
            @Override
            public Class<BigDecimal> handledType() {
                return BigDecimal.class;
            }

            @Override
            public void serialize(BigDecimal number, JsonGenerator generator, SerializerProvider serializer)
                    throws IOException {
                if (isSafelyNumber(number)) {
                    generator.writeNumber(number);
                } else {
                    generator.writeString(number.toPlainString());
                }
            }
        });

        // 设置本地日期序列化处理器
        this.addSerializer(new JsonSerializer<LocalDate>() {
            @Override
            public Class<LocalDate> handledType() {
                return LocalDate.class;
            }

            @Override
            public void serialize(LocalDate date, JsonGenerator generator, SerializerProvider serializer)
                    throws IOException {
                generator.writeObject(date);
            }
        });

        this.addSerializer(new CustomLocalDateTimeSerializer());
        this.addSerializer(new CustomLocalDateSerializer());

        // 设置localTime反序列化处理器
        this.addDeserializer(LocalTime.class, new JsonDeserializer<>() {

            @Override
            public Class<?> handledType() {
                return LocalTime.class;
            }

            @Override
            public LocalTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                return LocalTime.parse(p.getValueAsString(), formatter);
            }
        });

        // 设置时间周期反序列化处理器
        this.addDeserializer(Period.class, new JsonDeserializer<>() {
            @Override
            public Class<?> handledType() {
                return Period.class;
            }

            @Override
            public Period deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                // 获取当前JSON解析器的令牌类型，用于判断输入数据的格式
                JsonToken currentToken = parser.getCurrentToken();
                
                // 情况1：如果JSON值为null，直接返回null
                if (currentToken == JsonToken.VALUE_NULL) {
                    return null;
                }
                
                // 情况2：如果JSON值为字符串格式（如 "P1Y2M3D"），直接解析为Period对象
                if (currentToken == JsonToken.VALUE_STRING) {
                    return Period.parse(parser.getValueAsString());
                }
                
                // 情况3：如果JSON值为对象格式（如 {"begin": "2024-01-01T00:00:00", "end": "2024-12-31T23:59:59"}）
                if (currentToken == JsonToken.START_OBJECT) {
                    // 将JSON对象解析为树形结构，便于提取字段
                    JsonNode node = parser.getCodec().readTree(parser);
                    // 反序列化时间周期的开始边界（begin字段）
                    LocalDateTime begin = deserializePeriodBoundary(parser, node.get("begin"));
                    // 反序列化时间周期的结束边界（end字段）
                    LocalDateTime end = deserializePeriodBoundary(parser, node.get("end"));
                    
                    // 校验：开始时间和结束时间不能同时为空，至少需要提供一个边界
                    if (begin == null && end == null) {
                        throw MismatchedInputException.from(
                                parser,
                                Period.class,
                                "Period object must contain at least one of begin or end"
                                                           );
                    }
                    
                    // 尝试使用开始和结束时间创建Period对象
                    try {
                        return Period.of(begin, end);
                    } catch (IllegalArgumentException ex) {
                        // 如果参数不合法（如begin > end），抛出详细的输入异常信息
                        throw MismatchedInputException.from(parser, Period.class, ex.getMessage());
                    }
                }
                
                // 情况4：如果遇到不支持的JSON格式，抛出异常提示期望的格式
                throw MismatchedInputException.from(
                        parser,
                        Period.class,
                        "Expected Period as string or object, but got " + currentToken
                                                   );
            }
        });

        // 设置本地日期时间处理器
        this.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
            @Override
            public Class<?> handledType() {
                return LocalDateTime.class;
            }

            @Override
            public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                JsonToken currentToken = parser.getCurrentToken();
                if (currentToken == JsonToken.VALUE_STRING) {
                    return DateUtils.parse(parser.getValueAsString());
                }
                return DateUtils.parse(parser.getLongValue());
            }
        });
    }

    /**
     * 判断数字是否安全
     *
     * @param number 整数
     *
     * @return true/false
     */
    private static boolean isSafelyNumber(Long number) {
        return number == null || isSafelyNumber(BigInteger.valueOf(number));
    }

    /**
     * 判断数字是否安全
     *
     * @param number 浮点数
     *
     * @return true/false
     */
    private static boolean isSafelyNumber(Double number) {
        return number == null || isSafelyNumber(BigDecimal.valueOf(number));
    }

    /**
     * 判断数字是否安全
     *
     * @param number 大整数
     *
     * @return true/false
     */
    private static boolean isSafelyNumber(BigInteger number) {
        return number == null || number.compareTo(JS_MAX_INTEGER_VALUE) <= 0;
    }

    /**
     * 判断数字是否安全
     *
     * @param number 大浮点数
     *
     * @return true/false
     */
    private static boolean isSafelyNumber(BigDecimal number) {
        return number == null || number.compareTo(JS_MAX_DECIMAL_VALUE) <= 0;
    }

    private static LocalDateTime deserializePeriodBoundary(JsonParser parser, JsonNode node) throws IOException {
        if (node == null || node.isNull()) {
            return null;
        }
        return parser.getCodec().treeToValue(node, LocalDateTime.class);
    }
}
