package com.guhecloud.cube.srv.gen.maintain.server.electron.vo;

import lombok.Data;

@Data
public class PatrolAlarmVO {
    private String id;
    //类型
    private String alarmType;
    //地点
    private String alarmPoint;
    //内容
    private String alarmContent;
    //时间
    private String alarmTime;
    //处理结果
    private String alarmResult;
    //巡检记录ID
    private String patrolRecordId;

}
