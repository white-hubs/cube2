package com.guhecloud.cube.iot.mgt.core.data.vo.rule;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Jackson版本的Rule反序列化器
 *
 * @author fl
 * @date 2024/09/24
 **/
public class JacksonRuleValueDeserializer extends JsonDeserializer<List<Rule>> {
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Rule> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.getCurrentToken() == JsonToken.VALUE_NULL) {
            return Collections.emptyList();
        }
        
        List<Rule> ruleList = new ArrayList<>();
        
        if (parser.getCurrentToken() != JsonToken.START_ARRAY) {
            throw new IOException("规则列表必须是数组格式");
        }
        
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            JsonNode ruleNode = parser.getCodec().readTree(parser);
            
            // 根据JSON节点中的字段判断具体的Rule类型
            if (ruleNode.has("propCode")) {
                ruleList.add(objectMapper.treeToValue(ruleNode, SimpleRule.class));
            } else if (ruleNode.has("setValues")) {
                ruleList.add(objectMapper.treeToValue(ruleNode, SettingRule.class));
            } else if (ruleNode.has("rules")) {
                ruleList.add(objectMapper.treeToValue(ruleNode, ComboRule.class));
            }
        }
        
        return ruleList;
    }
}