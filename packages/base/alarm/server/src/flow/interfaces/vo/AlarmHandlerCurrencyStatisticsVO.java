package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.Data;

/**
 * 报警处理通用统计返回
 *
 * @author lanchun
 * @date 2022/11/10 11:19
 */
@Data
public class AlarmHandlerCurrencyStatisticsVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 已处理的报警数量
     */
    private Integer alarmHandlerTotalNum;

    /**
     * 未处理的报警数量
     */
    private Integer alarmUnresolvedHandlerTotalNum;
}
