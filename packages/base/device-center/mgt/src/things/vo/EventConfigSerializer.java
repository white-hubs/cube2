package com.guhecloud.cube.iot.mgt.things.vo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.guhecloud.cube.iot.mgt.core.data.vo.rule.TriggerType;

import java.io.IOException;

/**
 * EventConfig自定义序列化器
 *
 * 将EventConfig对象序列化为JSON，保持与原有格式一致
 */
public class EventConfigSerializer extends JsonSerializer<EventConfig> {

    @Override
    public void serialize(EventConfig eventConfig, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();

        // 序列化triggerType
        if (eventConfig.getTriggerType() != null) {
            gen.writeStringField("triggerType", eventConfig.getTriggerType().name());
        } else {
            gen.writeObjectFieldStart("triggerType");
            gen.writeEndObject();
        }

        // 序列化eventRelatedProp
        if (eventConfig.getEventRelatedProp() != null && !eventConfig.getEventRelatedProp().isEmpty()) {
            gen.writeObjectField("eventRelatedProp", eventConfig.getEventRelatedProp());
        } else {
            gen.writeArrayFieldStart("eventRelatedProp");
            gen.writeEndArray();
        }

        // 序列化rules
        if (eventConfig.getRules() != null) {
            gen.writeObjectField("rules", eventConfig.getRules());
        } else {
            gen.writeObjectFieldStart("rules");
            gen.writeEndObject();
        }

        // 序列化params
        if (eventConfig.getParams() != null && !eventConfig.getParams().isEmpty()) {
            gen.writeObjectField("params", eventConfig.getParams());
        } else {
            gen.writeArrayFieldStart("params");
            gen.writeEndArray();
        }

        gen.writeEndObject();
    }
}
