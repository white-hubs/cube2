package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("device_notify_rule")
public class DeviceNotifyRulePO extends BasicEnhancedTenantEntity<Long> {

    /**
     * 设备通知规则id
     */
    private Long id;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 物联规则id
     */
    private Long iotRuleId;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
