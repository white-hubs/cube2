package com.guhecloud.cube.srv.gen.attendance.management.server.clock.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassesClockStatDTO {
    private Long userId;
    private int realWorkDays;//实际出勤天数
    private int noClockNum;//缺卡次数
    private int noClockStartNum;//上班缺卡次数
    private int noClockEndNum;//下班缺卡次数
    private int startRepairClockNum;//上班补卡次数
    private int endRepairClockNum;//下班补卡次数
    private int repairClockNum;//补卡次数
    private int lateNum;//迟到次数
    private int lateTotalTime;//迟到分钟数
    private int earlyNum;//早退次数
    private int earlyTotalTime;//早退分钟数
    private int skipWorkNum;//旷工次数

    public int getNoClockNum() {
        return noClockStartNum + noClockEndNum;
    }

    public int getRepairClockNum() {
        return startRepairClockNum + endRepairClockNum;
    }
}
