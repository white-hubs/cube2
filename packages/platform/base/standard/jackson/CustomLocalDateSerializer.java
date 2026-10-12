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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATE_FORMAT;

public class CustomLocalDateSerializer extends JsonSerializer<LocalDate>
        implements ContextualSerializer {

    private final String pattern;
    private final String timezone;

    public CustomLocalDateSerializer() {
        this(DEFAULT_DATE_FORMAT, TimeZone.GMT8);
    }

    public CustomLocalDateSerializer(String pattern, String timezone) {
        this.pattern = pattern;
        this.timezone = timezone;
    }

    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        gen.writeString(DateUtils.format(LocalDateTime.of(value, LocalTime.MIN).atZone(ZoneId.of(timezone))
                                                 .toLocalDateTime(), pattern));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        if (property == null) {
            return new CustomLocalDateSerializer(DEFAULT_DATE_FORMAT, TimeZone.GMT8);
        }
        var jsonFormat = property.getAnnotation(JsonFormat.class);
        if (jsonFormat != null) {
            var pattern = Optional.ofNullable(jsonFormat.pattern()).orElse(DEFAULT_DATE_FORMAT);
            var timezone = Optional.ofNullable(jsonFormat.timezone()).orElse(TimeZone.GMT8);
            return new CustomLocalDateSerializer(pattern, timezone);
        }
        return this;
    }
}
