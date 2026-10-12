package com.guhecloud.cube.biz.emergency.server.facade.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件写值参数
 *
 * @author lanchun
 * @date 2024/4/17 16:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleWriteEventDTO {

    /**
     * 主键(句柄)
     */
    private Long id;

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 事件编号
     */
    private String eventCode;

    /**
     * 事件参数名
     */
    private String thresholdCode;

    /**
     * 事件参数值
     */
    private String thresholdValue;

}
