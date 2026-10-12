package com.guhecloud.cube.biz.emergency.server.facade.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 事件定义参数
 *
 * @author lanchun
 * @date 2022/11/15 16:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDefinitionDTO {

    private Long deviceId;

    /**
     * 设备型号主键
     */
    private Long modelId;

    /**
     * 设备事件编号
     */
    private String deviceEventCode;

    /**
     * 设备事件名称
     */
    private String deviceEventName;

    /**
     * 设备事件类型
     */
    private String deviceEventType;

    /**
     * 事件属性
     */
    private List<EventDefinitionPropDTO> eventDefinitionPropDTOS;
}
