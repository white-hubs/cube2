package com.guhecloud.cube.biz.emergency.server.time.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName("emergency_rule_writer_event")
@EqualsAndHashCode(callSuper = true)
public class EmergencyRuleWriterEventPO extends BasicEnhancedTenantEntity<Long> {

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
     * 设备主键
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 事件编号
     */
    private String eventCode;

    /**
     * 事件参数名
     */
    private String thresholdCode;

    /**
     * 事件参数值
     */
    private String thresholdValue;

    /**
     * 写值状态
     */
    private String writeStatus;

    /**
     * 写值有效开始时间
     */
    private LocalDateTime startTime;

    /**
     * 写值有效结束时间
     */
    private LocalDateTime endTime;

    /**
     * 重试次数
     */
    private Integer retryCount;
}
