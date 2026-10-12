package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/1 15:36
 * @description
 */
@Data
@TableName("device_health_problem")
public class DeviceHealthProblemPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    @TableField("`desc`")
    private String desc;
    private Long deviceHealthValueId;

    public DeviceHealthProblemPO() {
        this.id = IdWorker.getId();
    }
}
