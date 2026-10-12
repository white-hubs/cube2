package com.guhecloud.cube.base.alarm.server.sat.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 平台报警类型访问端数据模型
 *
 * @author lanchun
 * @date 2022/6/2 14:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SatAlarmTypeTerminal extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 报警类型主键
     */
    private Long alarmTypeId;

    /**
     * 访问端
     */
    private String terminal;

    /**
     * 访问端名称
     */
    private String terminalName;
}
