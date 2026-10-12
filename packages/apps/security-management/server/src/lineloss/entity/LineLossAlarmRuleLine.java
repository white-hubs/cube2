package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity;

import com.guhecloud.platform.database.entity.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报警规则的线路
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LineLossAlarmRuleLine extends BasicEntity<Long> {
    private Long id;
    /**
     * 报警规则id
     */
    private Long alarmRuleId;
    /**
     * 线路id
     */
    private Long lineId;
}
