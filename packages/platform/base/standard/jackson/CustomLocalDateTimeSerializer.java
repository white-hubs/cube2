package com.guhecloud.platform.base.standard.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.guhecloud.platform.base.constants.TimeZone;
import com.guhecloud.platform.base.util.DateUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATETIME_FORMAT;

public class CustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime>
        implements ContextualSerializer {

    private final String pattern;
    private final String timezone;

    public CustomLocalDateTimeSerializer() {
        this(null, null);
    }

    public CustomLocalDateTimeSerializer(String pattern, String timezone) {
        this.pattern = pattern;
        this.timezone = timezone;
    }

    @Override
    public Class<LocalDateTime> handledType() {
        return LocalDateTime.class;
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        if (pattern == null) {
            gen.writeNumber(DateUtils.toTimestamp(value));
            return;
        }
        gen.writeString(DateUtils.format(value.atZone(ZoneId.of(timezone)).toLocalDateTime(), pattern));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        if (property == null) {
            return new CustomLocalDateTimeSerializer(DEFAULT_DATETIME_FORMAT, TimeZone.GMT8);
        }
        var jsonFormat = property.getAnnotation(JsonFormat.class);
        if (jsonFormat != null) {
            var pattern = Optional.ofNullable(jsonFormat.pattern()).orElse(DEFAULT_DATETIME_FORMAT);
            var timezone = Optional.ofNullable(jsonFormat.timezone()).orElse(TimeZone.GMT8);
            return new CustomLocalDateTimeSerializer(pattern, timezone);
        }
        return this;
    }
}