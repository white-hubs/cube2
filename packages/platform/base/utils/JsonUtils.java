package com.guhecloud.platform.base.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.guhecloud.platform.base.standard.jackson.ObjectMapperConfig;

import java.io.IOException;
import java.util.List;

/**
 * 基于Jackson封装的json工具类
 */
public final class JsonUtils {
    static final ObjectMapper objectMapper = ObjectMapperConfig.getDefaultObjectMapper();

    private JsonUtils() {}

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * 对象序列化
     *
     * @param object 目标对象实例
     *
     * @return JSON字符串
     */
    public static String serialize(Object object) {
        try {
            return object == null ? null : getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象序列化
     *
     * @param object 目标对象实例
     *
     * @return JSON字符串
     */
    public static byte[] serializeBytes(Object object) {
        try {
            return object == null ? null : getObjectMapper().writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象反序列化
     *
     * @param json JSON字符串
     * @param type 类型对象
     * @param <T>  对象类型
     *
     * @return 对象实例
     */
    public static <T> T deserialize(String json, Class<T> type) {
        try {
            return StringUtils.isBlank(json) ? null : getObjectMapper().readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象反序列化
     *
     * @param json JSON字符串
     * @param type 类型引用对象
     * @param <T>  对象类型
     *
     * @return 对象实例
     */
    public static <T> T deserialize(String json, TypeReference<T> type) {
        try {
            return StringUtils.isBlank(json) ? null : getObjectMapper().readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserializeToBean(String json, TypeReference<T> type) throws JsonProcessingException {
        return StringUtils.isBlank(json) ? null : getObjectMapper().readValue(json, type);
    }

    /**
     * 对象反序列化
     *
     * @param bytes JSON字节数组
     * @param type  类型对象
     * @param <T>   对象类型
     *
     * @return 对象实例
     */
    public static <T> T deserialize(byte[] bytes, Class<T> type) {
        try {
            return bytes == null ? null : getObjectMapper().readValue(bytes, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象反序列化
     *
     * @param bytes JSON字节数组
     * @param type  类型引用对象
     * @param <T>   对象类型
     *
     * @return 对象实例
     */
    public static <T> T deserialize(byte[] bytes, TypeReference<T> type) {
        try {
            return bytes == null ? null : getObjectMapper().readValue(bytes, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象反序列化
     *
     * @param o    对象实例
     * @param type 类型对象
     * @param <T>  对象类型
     *
     * @return 对象实例
     */
    public static <T> T deserialize(Object o, Class<T> type) {
        try {
            return o == null ? null : getObjectMapper().convertValue(o, type);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化为集合
     *
     * @param json  JSON字符串
     * @param clazz 类型对象
     * @param <T>   对象类型
     *
     * @return 对象实例
     */
    public static <T> List<T> deserializeToList(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(List.class, clazz);
        try {
            return getObjectMapper().readValue(json, javaType);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 解析JSON字符串为JsonNode
     *
     * @param json JSON字符串
     *
     * @return JsonNode对象
     */
    public static JsonNode deserialize(String json) {
        try {
            return StringUtils.isBlank(json) ? null : getObjectMapper().readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JsonNod转指定Java对象
     *
     * @param node  JsonNode对象
     * @param clazz 指定Java对象
     * @param <T>   对象类型
     *
     * @return 指定Java对象
     */
    public static <T> T deserialize(JsonNode node, Class<T> clazz) {
        try {
            return getObjectMapper().treeToValue(node, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取json中指定key的值
     */
    public static JsonNode getNodeValue(String json, String fieldName) {
        try {
            JsonNode jsonNode = getObjectMapper().readTree(json);
            return jsonNode.get(fieldName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getNodeStringValue(String json, String fieldName) {
        try {
            JsonNode jsonNode = getObjectMapper().readTree(json);
            return getNodeValue(jsonNode, fieldName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getNodeStringLoopValue(String json, String fieldName) {
        try {
            JsonNode jsonNode = getObjectMapper().readTree(json);
            JsonNode value = getNodeLoopValue(jsonNode, fieldName);
            return value == null ? null : value.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取json中指定key的值并转换为指定对象
     */
    public static <T> T getNodeValue(String json, String fieldName, Class<T> clazz) {
        try {
            JsonNode jsonNode = getObjectMapper().readTree(json);
            return getNodeValue(jsonNode, fieldName, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从JsonNode中提取特定字段
     */
    public static String getNodeValue(JsonNode node, String fieldName) {
        JsonNode valueNode = node.get(fieldName);
        return valueNode != null ? valueNode.asText() : null;
    }

    /**
     * 从JsonNode中提取特定字段并转换为指定对象
     */
    public static <T> T getNodeValue(JsonNode node, String fieldName, Class<T> clazz) {
        JsonNode valueNode = node.get(fieldName);
        return deserialize(valueNode, clazz);
    }

    /**
     * 从JsonNode中递归提取特定字段对应的子节点
     */
    public static JsonNode getNodeLoopValue(JsonNode node, String fieldName) {
        if (node == null || StringUtils.isBlank(fieldName)) {
            return null;
        }
        if (node.isObject()) {
            JsonNode valueNode = node.get(fieldName);
            if (valueNode != null) {
                return valueNode;
            }
        }
        for (JsonNode childNode : node) {
            JsonNode valueNode = getNodeLoopValue(childNode, fieldName);
            if (valueNode != null) {
                return valueNode;
            }
        }
        return null;
    }

    /**
     * 从JsonNode中提取特定字段并转换为指定对象集合
     *
     * @param node
     * @param fieldName
     * @param clazz
     * @param <T>
     *
     * @return
     */
    public static <T> List<T> deserializeToList(JsonNode node, String fieldName, Class<T> clazz) {
        try {
            JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(List.class, clazz);
            JsonNode valueNode = node.get(fieldName);
            return getObjectMapper().treeToValue(valueNode, javaType);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 修改JsonNode并转换为JSON字符串
     */
    public static String modifyNode(String json, String fieldName, Object newValue) {
        try {
            ObjectNode node = (ObjectNode) getObjectMapper().readTree(json);

            if (newValue instanceof String) {
                node.put(fieldName, (String) newValue);
            } else if (newValue instanceof Integer) {
                node.put(fieldName, (Integer) newValue);
            } else if (newValue instanceof Boolean) {
                node.put(fieldName, (Boolean) newValue);
            } else if (newValue instanceof Double) {
                node.put(fieldName, (Double) newValue);
            } else {
                node.putPOJO(fieldName, newValue);
            }

            return node.toString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param o
     * @param type
     * @param <T>
     *
     * @return
     */
    public static <T> T deserialize(Object o, TypeReference<T> type) {
        try {
            return o == null ? null : getObjectMapper().convertValue(o, type);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(JsonNode jsonNode, String value, TypeReference<T> type) {
        try {
            JsonNode valueNode = jsonNode.get(value);
            return valueNode == null ? null : getObjectMapper().convertValue(valueNode, type);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

}
