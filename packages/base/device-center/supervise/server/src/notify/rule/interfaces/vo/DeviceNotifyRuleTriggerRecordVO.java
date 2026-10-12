package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceNotifyRuleTriggerRecordVO {

    private Long id;

    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 设备id
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
     * 产品分类名称
     */
    private String productCategoryName;

    /**
     * 产品名称
     */
    private String prodName;

    /**
     * 触发内容
     */
    private String triggerContent;

    /**
     * 触发时间
     */
    private LocalDateTime triggerTime;

    /**
     * 读取状态
     */
    private Boolean readStatus;
}