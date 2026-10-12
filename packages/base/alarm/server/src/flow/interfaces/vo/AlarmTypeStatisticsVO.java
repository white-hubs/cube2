package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.Data;

/**
 * 报警类型统计返回参数
 *
 * @author lanchun
 * @date 2022/6/7 14:07
 */
@Data
public class AlarmTypeStatisticsVO {

    /**
     * 报警类型主键
     */
    private String id;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 报警类型名称
     */
    private String name;

    /**
     * 报警数量
     */
    private Long alarmNum;
}
