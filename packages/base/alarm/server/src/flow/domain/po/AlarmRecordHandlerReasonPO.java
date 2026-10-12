package com.guhecloud.cube.base.alarm.server.flow.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("alarm_record_handler_reason")
public class AlarmRecordHandlerReasonPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 报警记录主键
     */
    private Long alarmRecordId;

    /**
     * 报警原因主键
     */
    private Long alarmReasonId;

    /**
     * 报警原因编号
     */
    private String alarmReasonCode;

    /**
     * 报警原因
     */
    private String alarmReason;

    /**
     * 报警原因描述
     */
    private String alarmReasonRemark;
}
