package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 市场统计卡片
 * @date 2023/9/7 19:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SafetyMarketStatisticsCardVO {
    /**
     * 安全任务数
     */
    private Integer safetyTaskNum;
    /**
     * 报警任务数
     */
    private Integer alarmTaskNum;
    /**
     * 隐患任务数
     */
    private Integer hiddenTaskNum;
    /**
     * 巡检任务数
     */
    private Integer inspectTaskNum;
    /**
     * 巡更任务数
     */
    private Integer patrolTaskNum;
    /**
     * 安全教育任务数
     */
    private Integer educationTaskNum;
    /**
     * 小散任务数
     */
    private Integer projectNum;
    /**
     * 预案文件数
     */
    private Integer planFileNum;
    /**
     * 建档设备数
     */
    private Integer deviceFilingNum;

}
