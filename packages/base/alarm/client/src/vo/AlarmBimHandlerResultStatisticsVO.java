package com.guhecloud.cube.base.alarm.client.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报警处理结果可视化统计
 *
 * @author lanchun
 * @date 2023/11/14 17:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmBimHandlerResultStatisticsVO {

    private Long totalNum;

    /**
     * 报警比率(可能是环比或占比根据实际情况使用)
     */
    private Float ratio;

    /**
     * 报警正确数量
     */
    private Long actualNum;

    /**
     * 正确比率(可能是环比或占比根据实际情况使用)
     */
    private Float actualRatio;

    /**
     * 报警误报数量
     */
    private Long impracticalNum;

    /**
     * 误报比率(可能是环比或占比根据实际情况使用)
     */
    private Float impracticalRatio;
}
