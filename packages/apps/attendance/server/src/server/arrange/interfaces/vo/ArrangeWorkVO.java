package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.dto.ClassesDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.dto.LeaveDetailDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/16
 * @description 排班
 */
@Data
public class ArrangeWorkVO {

    private Long id;

    /**
     * 员工ID
     */
    private Long staffId;

    /**
     * 员工姓名
     */
    private String staffName;
    /**
     * 部门id
     */
    private Long staffOrganizationId;
    //工号
    private String staffNo;
    //部门名称
    private String deptName;
    //考勤组id
    private Long groupId;
    //考勤组名称
    private String groupName;

    /**
     * 员工电话
     */
    private String staffMobile;
    // 考勤汇总信息
    private int arrangeWorkDays;//应出勤天数
    private int realWorkDays;//实际出勤天数
    private int noClockStartNum;//上班缺卡次数
    private int noClockEndNum;//下班缺卡次数
    private int noClockNum;//缺卡次数
    private int startRepairClockNum;//上班补卡次数
    private int endRepairClockNum;//下班补卡次数
    private int repairClockNum;//补卡次数
    private int lateNum;//迟到次数
    private int earlyNum;//早退次数
    private int skipWorkNum;//旷工次数
    private int lateTotalTime;//迟到分钟数
    private int earlyTotalTime;//早退分钟数
    // 加班汇总信息
    private float overtimeWorkDuration;//工作日加班时长
    private float overtimeRestDuration;//休息日加班时长
    private float overtimeFestDuration;//节假日加班时长
    // 离岗汇总信息
    private float leavePostDuration;//离岗时长

    /**
     * 每日排班数据
     */
    private List<DateOfClasses> dayOfClasses;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DateOfClasses {

        public DateOfClasses(LocalDateTime date) {
            this.date = date;
        }

        /**
         * 月份
         */
        private String month;

        /**
         * 周几
         */
        private Integer week;

        /**
         * 日期
         */
        private LocalDateTime date;

        /**
         * 打卡状态
         */
        private String clockStatus;

        /**
         * 审批触发状态
         */
        private String trigger;

        /**
         * 班次信息
         */
        private List<Classes> classesVOS;

        /**
         * 班次信息
         */
        private List<ClassesDTO> classesDTOList;

        /**
         * 请假集合
         */
        List<LeaveDetailDTO> leaveDetailDTOS;

    }

    public int getNoClockNum() {
        return noClockStartNum + noClockEndNum;
    }

    public int getRepairClockNum() {
        return this.startRepairClockNum + this.endRepairClockNum;
    }
}