package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 超时告警数量统计
 */
@Getter
@SuperBuilder
public class AlarmMarketTimeoutCountVO extends AlarmTimeoutCountVO {
    private String marketTenantID;

    private String marketName;
}
