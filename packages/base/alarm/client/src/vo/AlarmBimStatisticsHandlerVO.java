package com.guhecloud.cube.base.alarm.client.vo;

import lombok.Data;

/**
 * 报警处理相关统计
 *
 * @author lanchun
 * @date 2023/11/15 11:43
 */
@Data
public class AlarmBimStatisticsHandlerVO {

    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 总数
     */
    private Integer totalNum;

    /**
     * 处理数
     */
    private Integer handlerNum;

    /**
     * 平均处理时长(秒数)
     */
    private Integer processingTime;
}
