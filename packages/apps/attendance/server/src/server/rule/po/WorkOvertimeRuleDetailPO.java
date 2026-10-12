package com.guhecloud.cube.srv.gen.attendance.management.server.rule.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author bq
 * @date 2022/11/10
 * @description 加班规则细则
 */
@Data
@TableName("work_overtime_rule_detail")
public class WorkOvertimeRuleDetailPO {

    private Long id;

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 加班类型(工作日、休息日、节假日)
     */
    private String type;

    /**
     * 计算方式
     */
    private String method;

    /**
     * 下班后多少分钟开始计算
     */
    private Integer afterTime;

    /**
     * 加班补偿计算方式
     */
    private String compensationMethod;

    /**
     * 加班补偿方案
     */
    private String compensate;

    /**
     * 计算调休比例
     */
    private String proportion;
    /**
     * 加班审批条件
     */
    private String workCondition;
    /**
     * 计算单位(半小时、小时、天)
     */
    private String units;

}