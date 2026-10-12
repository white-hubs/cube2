package com.guhecloud.cube.biz.emergency.server.time.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.emergency.server.time.core.eumeration.EmergencyRuleTimeExecuteStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName("emergency_rule_time_job_log")
@EqualsAndHashCode(callSuper = true)
public class EmergencyRuleTimeJobLogPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    private Long emergencyRuleTimeJobId;

    /**
     * 开启规则or关闭规则
     */
    private String openOrClose;

    /**
     * 执行状态
     */
    private EmergencyRuleTimeExecuteStatusEnum executeStatus;

    /**
     * 触发时间
     */
    private LocalDateTime triggerTime;

    /**
     * 处理时间
     */
    private LocalDateTime handlerTime;

    /**
     * 错误信息
     */
    private String errorMsg;
}
