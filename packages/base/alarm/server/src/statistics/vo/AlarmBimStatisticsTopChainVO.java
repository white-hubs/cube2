package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用报警排名环比返回参数
 *
 * @author lanchun
 * @date 2023/3/16 14:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmBimStatisticsTopChainVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 报警数量
     */
    private Integer alarmNum;

    /**
     * 环比率
     */
    private double chainRatio;

    /**
     * 占比
     */
    private double ratio;
}
