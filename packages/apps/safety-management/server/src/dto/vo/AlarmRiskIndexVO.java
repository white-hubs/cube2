package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 报警风险指数计算
 * @date 2023/10/16 10:14
 */
@Data
public class AlarmRiskIndexVO {
    /**
     * 报警级别
     */
    private int alarmLevel;
    /**
     * 报警状态
     */
    private String alarmStatus;
}
