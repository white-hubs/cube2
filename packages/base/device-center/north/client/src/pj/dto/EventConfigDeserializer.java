package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.param.ParamSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * EventConfig自定义反序列化器
 *
 * 根据triggerType字段动态选择Rule的具体类型进行反序列化
 */
public class EventConfigDeserializer extends JsonDeserializer<EventConfig> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public EventConfig deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        EventConfig eventConfig = new EventConfig();

        // 解析triggerType
        JsonNode triggerTypeNode = node.get("triggerType");
        if (triggerTypeNode != null && !triggerTypeNode.isNull()) {
            String triggerTypeStr = triggerTypeNode.asText();
            eventConfig.setTriggerType(TriggerType.valueOf(triggerTypeStr));
        }

        // 解析eventRelatedProp
        JsonNode eventRelatedPropNode = node.get("eventRelatedProp");
        if (eventRelatedPropNode != null && eventRelatedPropNode.isArray()) {
            List<String> eventRelatedProp = new ArrayList<>();
            for (JsonNode propNode : eventRelatedPropNode) {
                eventRelatedProp.add(propNode.asText());
            }
            eventConfig.setEventRelatedProp(eventRelatedProp);
        }

        // 解析rules - 根据triggerType选择具体的Rule类型
        JsonNode rulesNode = node.get("rules");
        if (rulesNode != null && !rulesNode.isNull() && !rulesNode.isEmpty()) {
            Rule rule = parseRule(rulesNode, eventConfig.getTriggerType());
            eventConfig.setRules(rule);
        }

        // 解析params
        JsonNode paramsNode = node.get("params");
        if (paramsNode != null && !paramsNode.isNull() && !paramsNode.isEmpty()) {
            JsonNode paramsArrayNode = paramsNode.get("params");
            if (paramsArrayNode != null && paramsArrayNode.isArray()) {
                List<ParamSpec> paramList = OBJECT_MAPPER.treeToValue(
                    paramsArrayNode,
                    new TypeReference<List<ParamSpec>>() {}
                );
                eventConfig.setParams(paramList);
            }
        }

        return eventConfig;
    }

    /**
     * 根据triggerType解析Rule对象
     *
     * @param rulesNode   规则JSON节点
     * @param triggerType 触发类型
     * @return Rule对象
     */
    private Rule parseRule(JsonNode rulesNode, TriggerType triggerType) throws IOException {
        if (triggerType == null) {
            return null;
        }

        return switch (triggerType) {
            case SIMPLE -> OBJECT_MAPPER.treeToValue(rulesNode, SimpleRule.class);
            case SETTING -> OBJECT_MAPPER.treeToValue(rulesNode, SettingRule.class);
            default ->
                // NATURE和COMBO类型不需要解析rules
                    null;
        };
    }
}
