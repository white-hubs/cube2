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
@TableName("health_alarm_rule")
public class HealthAlarmRulePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Integer thresholdValue;
    private Boolean enable;
    private Long levelId;
    private Long alarmTypeId;
    private Long triggeredRuleId;

    public HealthAlarmRulePO() {
        this.id = IdWorker.getId();
    }
}
