package com.guhecloud.cube.iot.mgt.things.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.iot.mgt.core.data.vo.rule.Rule;
import com.guhecloud.cube.iot.mgt.core.data.vo.rule.TriggerType;
import com.guhecloud.cube.iot.mgt.things.vo.param.ParamSpec;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 事件配置信息<br/>
 * 包含Rule、RelatedProp
 *
 * @author fl
 * @date 2022/10/20
 **/
@Data
@JsonSerialize(using = EventConfigSerializer.class)
@JsonDeserialize(using = EventConfigDeserializer.class)
public class EventConfig {

    /**
     * 事件规则类型
     */
    private TriggerType triggerType;

    /**
     * 保存属性CODE
     */
    private List<String> eventRelatedProp;

    private Rule rules;

    private List<ParamSpec> params;

    public static EventConfig buildNatureEventConfig() {
        EventConfig eventConfig = new EventConfig();
        eventConfig.setTriggerType(TriggerType.NATURE);
        eventConfig.setEventRelatedProp(Collections.emptyList());
        return eventConfig;
    }
}
