package com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo;

import lombok.Data;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤组补卡规则
 */
@Data
public class AttendanceGroupSupplementCardRuleVO extends SupplementCardRuleVO {

    private Long id;

    /**
     * 考勤组ID
     */
    private Long groupId;

    /**
     * 补卡规则ID
     */
    private Long supplementCardRuleId;

}