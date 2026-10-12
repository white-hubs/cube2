package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("device_notify_rule_trigger_record")
public class DeviceNotifyRuleTriggerRecordPO extends BasicEnhancedTenantEntity<Long> {

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
     * 产品分类名称
     */
    private String productCategoryName;

    /**
     * 产品名称
     */
    private String prodName;

    /**
     * 设备名称
     */
    private String deviceName;

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
