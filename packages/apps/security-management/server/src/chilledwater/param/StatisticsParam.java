package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import lombok.Data;

@Data
public class StatisticsParam {
    private String startTime;
    private String endTime;
    private String timeFlag;
    //部门
    private String department;
    //档位号
    private String gearPositionCode;
    //钛泡编码
    private String deviceCode;
    //档主编号
    private String code;
    //档主姓名
    private String shiftMaster;
    //时间类型
    private String timeType;
}
