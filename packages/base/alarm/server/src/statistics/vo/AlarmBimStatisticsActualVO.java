package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报警结果正确率统计
 *
 * @author lanchun
 * @date 2023/11/15 14:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmBimStatisticsActualVO {

    /**
     * 编号
     */
    private String code;

    /**
     * 真实报警数量
     */
    private Integer actualNum;

    /**
     * 已处理总数
     */
    private Integer handlerNum;

    /**
     * 正确占比
     */
    private float ratio;
}
