package com.guhecloud.cube.base.alarm.server.sat.entity;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报警类型对应状态配置
 *
 * @author lanchun
 * @date 2023/5/29 11:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SatAlarmTypeStatus extends BasicEnhancedEntity<Long> {

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
     * 是否是初始化的状态
     */
    private Boolean initialized;

}
