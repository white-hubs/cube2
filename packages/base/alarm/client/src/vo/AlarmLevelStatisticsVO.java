package com.guhecloud.cube.base.alarm.client.vo;

import lombok.Data;

/**
 * 报警级别统计返回参数
 *
 * @author lanchun
 * @date 2022/6/7 14:07
 */
@Data
public class AlarmLevelStatisticsVO {

    /**
     * 报警级别主键
     */
    private String id;

    /**
     * 报警级别编号
     */
    private String code;

    /**
     * 报警级别名称
     */
    private String name;

    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 处理数量
     */
    private Long handlerNum;

    /**
     * 处理率
     */
    private Float handlerRate;

    /**
     * 占比
     */
    private String ratio;
}
