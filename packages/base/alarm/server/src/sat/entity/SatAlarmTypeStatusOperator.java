package com.guhecloud.cube.base.alarm.server.sat.entity;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmOperatorEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报警类型状态对应操作配置
 *
 * @author lanchun
 * @date 2023/5/29 11:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SatAlarmTypeStatusOperator extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 报警类型编号
     */
    private String alarmTypeCode;

    /**
     * 状态编号
     */
    private AlarmStatusEnum status;

    /**
     * 报警操作类型编号
     */
    private AlarmOperatorEnum operator;

    /**
     * 操作后的状态编号
     */
    private String operatorAfterStatus;
}
