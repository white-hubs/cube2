package com.guhecloud.cube.biz.emergency.server.time.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName("emergency_rule_writer_event_log")
@EqualsAndHashCode(callSuper = true)
public class EmergencyRuleWriterEventLogPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 规则主键
     */
    private Long emergencyRuleId;

    /**
     * 规则时段主键
     */
    private Long emergencyRuleTimeId;

    /**
     * 事件写值记录主键
     */
    private Long emergencyRuleWriterEventId;

    /**
     * 写值状态
     */
    private String iotWriteStatus;
}
