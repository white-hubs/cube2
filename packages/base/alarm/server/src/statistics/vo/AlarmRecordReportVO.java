package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.Data;

@Data
public class AlarmRecordReportVO {
    //总数
    private Long alarmNum;
    //应用名称
    private String applicationCode;
    //报警事件
    private String alarmEventName;
    //报警等级
    private String alarmLevelCode;
    //真实报警数
    private Long alarmActualNum;
    //响应报警数
    private Long respondedNum;
    //已处理报警数
    private Long alarmHandlerNum;
    //跨天处理数
    private Long outDayHandlerNum;
    //超市报警数
    private Long timeoutNum;
    //用户名
    private String userName;
    private String userId;
    //部门
    private String departmentName;
    private String departmentId;
    private Long departmentNum;
}
