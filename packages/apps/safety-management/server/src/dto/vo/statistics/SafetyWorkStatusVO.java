package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 安全工作情况
 * @date 2023/9/7 15:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SafetyWorkStatusVO {

    /**
     * 报警处理数
     */
    private Integer alarmDisposeNum;
    /**
     * 报警处理率
     */
    private Double alarmDispose;

    /**
     * 隐患处理数量
     */
    private Integer hiddenDangerDisposeNum;
    /**
     * 隐患处理率
     */
    private Double hiddenDangerDispose;

    /**
     * 巡检任务完成数量
     */
    private Integer inspectCompleteNum;
    /**
     * 巡检任务完成率
     */
    private Double inspectComplete;

    /**
     * 巡更任务完成数量
     */
    private Integer patrolCompleteNum;
    /**
     * 巡更任务完成率
     */
    private Double patrolComplete;

    /**
     * 小散工程完成数量
     */
    private Integer projectCompleteNum;
    /**
     * 小散工程异常率
     */
    private Double projectError;
    /**
     * 教育任务占比情况
     */
    private Double educationProportion;

}
