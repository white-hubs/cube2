package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/1 15:35
 * @description
 */
@Data
@TableName("device_health_value")
public class DeviceHealthValuePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long deviceId;
    private Integer healthValue;

    public DeviceHealthValuePO() {
        this.id = IdWorker.getId();
    }
}
