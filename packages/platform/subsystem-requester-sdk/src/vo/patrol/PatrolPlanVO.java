package com.guhecloud.subsystem.requester.sdk.vo.patrol;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatrolPlanVO {
    //计划ID
    private String id;
    //计划名称
    private String planName;
    //计划类型 有序无序
    private String planType;
    //计划模式（0常规、1班组、2星期、3周期计划）
    private String planMode;
    //开始时间
    private LocalDateTime startTime;
    //结束时间
    private LocalDateTime endTime;
    //线路ID
    private String lineId;
    //巡更人员
    private String userIds;//多个人员用逗号分割
    //巡更设备
    private String deviceIds;//多个设备用逗号分割
    //创建时间
    private LocalDateTime createTime;
}
