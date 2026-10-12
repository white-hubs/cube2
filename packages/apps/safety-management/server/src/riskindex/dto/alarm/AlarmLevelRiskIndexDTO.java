package com.guhecloud.cube.srv.gen.safety.management.riskindex.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报警级别得分情况
 *
 * @author lanchun
 * @date 2023/11/6 15:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmLevelRiskIndexDTO {

    /**
     * 报警级别编号
     */
    private String alarmLevel;

    /**
     * 分数
     */
    private int riskIndex;

    /**
     * 报警总数
     */
    private long totalNum;

}
