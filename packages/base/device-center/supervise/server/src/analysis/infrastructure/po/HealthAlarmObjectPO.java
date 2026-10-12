package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/5 10:53
 * @description
 */
@Data
@TableName("health_alarm_object")
public class HealthAlarmObjectPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long deviceInstanceId;
    private Long alarmRuleId;

    public HealthAlarmObjectPO() {
        this.id = IdWorker.getId();
    }
}
