package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command;

import lombok.Data;

/**
 * @author bq
 * @date 2022/11/15
 * @description 考勤组补卡规则
 */
@Data
public class SubmitAttendanceGroupSupplementCardRuleCommand {

    private Long id;

    /**
     * 考勤组ID
     */
    private Long groupId;

    /**
     * 补卡规则ID
     */
    private Long supplementCardRuleId;

    /**
     * 冗余补卡规则-名称
     */
    private String name;

    /**
     * 补卡规则-允许补卡类型
     */
    private String type;

    /**
     * 补卡规则-每月允许补卡次数
     */
    private Integer times;

    /**
     * 补卡规则-几日内允许补卡
     */
    private Integer days;

}