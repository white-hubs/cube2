package com.guhecloud.cube.base.alarm.server.flow.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStateEventEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 报警记录事件操作数据
 *
 * @author lanchun
 * @date 2022/6/6 10:25
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("alarm_state_event_operate")
public class AlarmStateEventOperatePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 报警记录主键
     */
    private Long alarmRecordId;

    /**
     * 报警状态
     */
    private AlarmStatusEnum alarmStatus;

    /**
     * 状态事件
     */
    private AlarmStateEventEnum alarmStatusEvent;

    /**
     * 操作参数
     */
    private String operationParam;
}
