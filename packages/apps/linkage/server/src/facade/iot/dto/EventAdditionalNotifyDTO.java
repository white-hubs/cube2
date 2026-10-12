package com.guhecloud.cube.biz.linkage.server.facade.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot事件附加信息请求参数
 *
 * @author lanchun
 * @date 2023/3/27 15:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventAdditionalNotifyDTO {

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 事件实例主键
     */
    private String eventInstanceId;

    /**
     * 事件编号
     */
    private String eventCode;

    /**
     * 附加信息
     */
    private EventAdditionalDTO additional;

    private Long eventTime;
}
