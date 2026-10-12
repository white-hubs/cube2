package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * @author bq
 * @date 2022/11/11
 * @description 提交加班规则
 */
@Data
public class SubmitWorkOvertimeRuleCommand {

    private Long id;

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    @Length(max = 20, message = "名称限制在20字内")
    private String name;
    /**
     * 加班上限周期(每天、每周、每月)
     */
    private String upperLimitCycle;

    /**
     * 加班上限累计时长(小时)
     */
    private Integer upperLimitDuration;

    /**
     * 规则细则
     */
    private List<Detail> ruleDetails;

    @Data
    public static class Detail {
        /**
         * 加班审批条件
         */
        private String workCondition;
        /**
         * 加班类型(工作日、休息日、节假日)
         */
        private String type;
        /**
         * 计算单位(半小时、小时、天)
         */
        private String units;

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
    }

}
