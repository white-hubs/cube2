package com.guhecloud.subsystem.requester.sdk.vo.patrol;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 巡检记录
 */
@Data
public class PatrolRecordVO {
    private String id;
    //计划开始时间
    private LocalDateTime startTime;
    //计划结束时间
    private LocalDateTime endTime;
    //巡检时间
    private LocalDateTime patrolTime;
    //线路ID
    private String lineId;
    //线路名称
    private String lineName;
    //巡检点ID
    private String pointId;
    //巡检点名称
    private String pointName;
    //线路顺序
    private Long lineOrder;
    //巡检人ID
    private String userId;
    //巡检人姓名
    private String userName;
    //巡检设备ID
    private String deviceId;
    //巡检设备名称
    private String deviceName;
    //巡检状态
    private String status;
    //停留时间
    private int stayTime;
    //创建时间
    private LocalDateTime createTime;
    //计划ID
    private String planId;
    //是否正常 true 正常 false 异常
    private Boolean normal;
}
