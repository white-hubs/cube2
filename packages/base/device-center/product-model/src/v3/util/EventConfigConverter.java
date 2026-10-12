package com.guhecloud.cube.base.iot.product.model.v3.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.iot.product.model.v3.dto.event.EventConfig;
import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 事件配置JSON转换工具类
 *
 * 用于EventConfig实体类与JSON字符串之间的转换
 */
@Slf4j
public class EventConfigConverter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将EventConfig对象转换为JSON字符串
     * 用于保存到数据库
     *
     * @param config EventConfig对象
     * @return JSON字符串,如果config为null则返回null
     */
    public static String toJson(EventConfig config) {
        if (config == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            log.error("EventConfig转JSON失败", e);
            throw new InvalidArgumentException("事件配置序列化失败", e);
        }
    }

    /**
     * 将JSON字符串转换为EventConfig对象
     * 用于从数据库读取
     *
     * @param json JSON字符串
     * @return EventConfig对象,如果json为空则返回null
     */
    public static EventConfig fromJson(String json) {
        if (!StringUtils.hasText(json)) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, EventConfig.class);
        } catch (JsonProcessingException e) {
            log.error("JSON转EventConfig失败, json={}", json, e);
            throw new InvalidArgumentException("事件配置反序列化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 校验EventConfig对象是否有效
     *
     * @param config EventConfig对象
     * @throws InvalidArgumentException 如果配置无效
     */
    public static void validate(EventConfig config) {
        if (config == null) {
            return;
        }

        if (config.getTriggerType() == null) {
            throw new InvalidArgumentException("事件触发类型不能为空");
        }

        // 可以根据需要添加更多校验规则
        // 例如：简单规则事件必须有rules配置等
    }
}
