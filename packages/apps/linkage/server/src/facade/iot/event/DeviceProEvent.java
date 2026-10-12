package com.guhecloud.cube.biz.linkage.server.facade.iot.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备属性事件
 *
 * @author lanchun
 * @date 2022/6/23 10:30
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceProEvent {

    /**
     * 设备主键
     */
    private String deviceCode;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性值
     */
    private String oprand;

    /**
     * 操作符
     */
    private String op;

    /**
     * 状态持续时延
     */
    private Integer delayTime;
}
