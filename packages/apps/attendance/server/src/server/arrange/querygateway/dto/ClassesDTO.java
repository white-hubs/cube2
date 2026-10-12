package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.dto;

import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity.LeaveFormDetail;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import lombok.Data;

@Data
public class ClassesDTO extends Classes {

    /**
     * 上班打卡时间
     */
    private String startClockTime;
    private String endClockTime;

    /**
     * 上下班打卡状态
     */
    private String startClockStatus;
    private String endClockStatus;

    /**
     * 上下班触发状态
     */
    private String startTrigger;
    private String endTrigger;

    /**
     * 上/下班迟到/早退分钟数
     */
    private Long startLateMinute;
    private Long endLateMinute;

    /**
     * 上下班补卡标记
     */
    private Boolean startRepairFlag;
    private Boolean endRepairFlag;

    /**
     * 请假标记
     */
    private Boolean leaveFlag;
    private Boolean startLeaveFlag;
    private Boolean endLeaveFlag;
    /**
     * 请假类型
     */
    private String leaveType;
    private String startLeaveType;
    private String endLeaveType;
    /**
     * 请假时间
     */
    private Float leaveHour;
    private Float startLeaveHour;
    private Float endLeaveHour;

    /**
     * 请假时间对象
     */
    private LeaveFormDetail.LeaveTime leaveTime;
    private LeaveFormDetail.LeaveTime startLeaveTime;
    private LeaveFormDetail.LeaveTime endLeaveTime;

}
