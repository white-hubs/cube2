package com.guhecloud.cube.srv.gen.attendance.management.server.rule.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤组班次
 */
@Data
@TableName("attendance_group_classes")
public class AttendanceGroupClassesPO {

    private Long id;

    /**
     * 考勤组ID
     */
    private Long groupId;

    /**
     * 时间（周几）
     */
    private Integer week;

    /**
     * 班次ID
     */
    private Long classesId;

    /**
     * 冗余班次信息-班次名称
     */
    private String name;

    /**
     * 冗余班次信息-上班时间
     */
    private String startWorkTime;

    /**
     * 冗余班次信息-上班时间前
     */
    private String beforeStartWorkTime;

    /**
     * 冗余班次信息-上班时间后
     */
    private String afterStartWorkTime;

    /**
     * 冗余班次信息-下班时间
     */
    private String endWorkTime;

    /**
     * 冗余班次信息-下班时间前
     */
    private String beforeEndWorkTime;

    /**
     * 冗余班次信息-下班时间后
     */
    private String afterEndWorkTime;

    /**
     * 冗余班次信息-休息开始时间
     */
    private String restStartTime;

    /**
     * 冗余班次信息-休息结束时间
     */
    private String restEndTime;

    /**
     * 冗余班次信息-标志
     */
    private String mark;

    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;

}