package com.guhecloud.cube.base.device.center.core.rule;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.device.center.core.rule.entity.RuleGroup;
import com.guhecloud.platform.base.util.JsonUtils;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 标签条件规则解析
 *
 * @author lanchun
 * @date 2025/4/9 14:00
 */
@Slf4j
public class RuleParse {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonSchema SCHEMA;
    private static final String JSON_SCHEMA_STR = "{\n" +
                                                  "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
                                                  "  \"type\": \"object\",\n" +
                                                  "  \"properties\": {\n" +
                                                  "    \"rules\": {\n" +
                                                  "      \"type\": \"array\",\n" +
                                                  "      \"items\": {\n" +
                                                  "        \"type\": \"object\",\n" +
                                                  "        \"properties\": {\n" +
                                                  "          \"ruleFields\": {\n" +
                                                  "            \"type\": \"array\",\n" +
                                                  "            \"items\": {\n" +
                                                  "              \"type\": \"object\",\n" +
                                                  "              \"properties\": {\n" +
                                                  "                \"field\": {\n" +
                                                  "                  \"type\": \"string\"\n" +
                                                  "                },\n" +
                                                  "                \"operator\": {\n" +
                                                  "                  \"type\": \"string\"\n" +
                                                  "                },\n" +
                                                  "                \"value\": {\n" +
                                                  "                  \"oneOf\": [\n" +
                                                  "                    {\n" +
                                                  "                      \"type\": \"object\"\n" +
                                                  "                    },\n" +
                                                  "                    {\n" +
                                                  "                      \"type\": \"integer\"\n" +
                                                  "                    },\n" +
                                                  "                    {\n" +
                                                  "                      \"type\": \"array\"\n" +
                                                  "                    },\n" +
                                                  "                    {\n" +
                                                  "                      \"type\": \"string\"\n" +
                                                  "                    }\n" +
                                                  "                  ]\n" +
                                                  "                }\n" +
                                                  "              },\n" +
                                                  "              \"required\": [\"field\", \"operator\", \"value\"]\n" +
                                                  "            }\n" +
                                                  "          }\n" +
                                                  "        },\n" +
                                                  "        \"required\": [\"ruleFields\"]\n" +
                                                  "      }\n" +
                                                  "    }\n" +
                                                  "  }\n" +
                                                  "}\n";

    static {
        //读取JSONSchema
        SCHEMA = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(JSON_SCHEMA_STR);
    }

    public static Boolean validate(String json) {
        try {
            JsonNode jsonNode = MAPPER.readTree(json);
            Set<ValidationMessage> validationMessages = SCHEMA.validate(jsonNode);
            if (!validationMessages.isEmpty()) {
                log.error("JSON schema validation failed: {}", validationMessages);
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to validation failed: {}", json, e);
            return false;
        }
    }

    public static RuleGroup parseRuleConfig(String json) {
        if (validate(json)) {
            return JsonUtils.deserialize(json, RuleGroup.class);
        }
        return null;
    }
}
