package com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo;

import lombok.Data;

/**
 * @author appet
 * @date 2022/11/14
 * @description 考勤组补卡规则
 */
@Data
public class AttendanceGroupRepairClockRuleVO {
    private Long id;
    private String name;
    private String type;//补卡的类型
    private Integer times;//每月补卡次数
    private Integer days;//补卡时间范围
}