package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.param.ParamSpec;
import lombok.Data;

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

    /**
     * 对外(API)暴露的属性
     */
    private Rule rules;

    /**
     * 其他参数
     */
    @JsonProperty("params")
    private List<ParamSpec> params;
}
