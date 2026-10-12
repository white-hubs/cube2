package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.Data;

/**
 * 报警状态分页统计
 *
 * @author lanchun
 * @date 2022/11/8 16:28
 */
@Data
public class AlarmEventStatusStatisticsPageVO {

    /**
     * 报警事件
     */
    private String alarmEvent;

    /**
     * 报警事件名称
     */
    private String alarmEventName;

    /**
     * 报警总数
     */
    private Long totalNum;

    /**
     * 待接收报警数
     */
    private Long unConfirmedNum;

    /**
     * 处理中报警数
     */
    private Long progressConfirmedNum;

    /**
     * 已挂起报警数
     */
    private Long suspendNum;

    /**
     * 已处理报警数
     */
    private Long handlerNum;

    /**
     * 超时报警数
     */
    private Long timeoutNum;
}
