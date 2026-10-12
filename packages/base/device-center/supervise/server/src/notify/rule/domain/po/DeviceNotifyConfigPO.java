package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("device_notify_config")
public class DeviceNotifyConfigPO extends BasicEnhancedTenantEntity<Long> {

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
     * 通知配置
     */
    private String notifyConfig;
}
