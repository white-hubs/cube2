package com.guhecloud.cube.base.iot.product.model.v3.dto.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.cube.base.iot.product.model.v3.enums.EventTriggerTypeEnum;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 事件配置
 *
 * 定义产品事件的完整配置信息,包括触发类型和规则
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventConfig {

    /**
     * 触发类型
     * - NATURE: 原生事件,设备主动上报
     * - SIMPLE: 简单规则事件,通过本地规则生成
     */
    private EventTriggerTypeEnum triggerType;

    /**
     * 事件关联属性列表
     * 暂时为空数组,预留字段用于未来扩展
     * 可能用于关联需要监听的属性编码
     */
    private List<String> eventRelatedProp;

    /**
     * 事件规则
     * - 当 triggerType = NATURE 时,为空对象 new EventRule()
     * - 当 triggerType = SIMPLE 时,包含具体的规则配置
     */
    private EventRule rules;

    /**
     * 创建原生事件配置
     *
     * @return 原生事件配置实例
     */
    public static EventConfig createNatureConfig() {
        EventConfig config = new EventConfig();
        config.setTriggerType(EventTriggerTypeEnum.NATURE);
        config.setEventRelatedProp(Collections.emptyList());
        config.setRules(new EventRule());
        return config;
    }

    /**
     * 创建简单规则事件配置
     *
     * @param rule 事件规则
     * @return 简单规则事件配置实例
     */
    public static EventConfig createSimpleConfig(EventRule rule) {
        EventConfig config = new EventConfig();
        config.setTriggerType(EventTriggerTypeEnum.SIMPLE);
        config.setEventRelatedProp(Collections.emptyList());
        config.setRules(rule);
        return config;
    }
}
