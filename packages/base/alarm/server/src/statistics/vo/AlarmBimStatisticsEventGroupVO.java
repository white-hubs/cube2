package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.Data;

/**
 * 可视化事件分组统计返回信息
 *
 * @author lanchun
 * @date 2023/11/15 15:54
 */
@Data
public class AlarmBimStatisticsEventGroupVO {

    /**
     * 事件主键
     */
    private String id;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 总数
     */
    private Integer totalNum;
}
