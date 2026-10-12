package com.guhecloud.cube.base.alarm.server.analysis.vo;

import lombok.Data;

@Data
public class AlarmHandlerRankCardVO1 {

    /**
     * 当前角色负责报警数
     */
    private Long alarmNum;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 响应报警数
     */
    private Long respondedNum;

    /**
     * 已处理报警数
     */
    private Long handlerNum;

    /**
     * 超时报警数
     */
    private Long timeoutNum;

}
