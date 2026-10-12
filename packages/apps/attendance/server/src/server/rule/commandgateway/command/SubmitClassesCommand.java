package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author bq
 * @date 2022/11/10
 * @description 提交班次信息
 */
@Data
public class SubmitClassesCommand {

    private Long id;

    /**
     * 名称
     */
    @Length(max = 20, message = "名称限制在20字内")
    @NotEmpty(message = "名称不能为空")
    private String name;

    /**
     * 上班时间
     */
    @NotEmpty(message = "上班时间不能为空")
    private String startWorkTime;

    /**
     * 上班时间前
     */
    private String beforeStartWorkTime;

    /**
     * 上班时间后
     */
    private String afterStartWorkTime;

    /**
     * 下班时间
     */
    @NotEmpty(message = "下班时间不能为空")
    private String endWorkTime;

    /**
     * 下班时间前
     */
    private String beforeEndWorkTime;

    /**
     * 下班时间后
     */
    private String afterEndWorkTime;

    /**
     * 休息开始时间
     */
    private String restStartTime;

    /**
     * 休息结束时间
     */
    private String restEndTime;

    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;

    /**
     * 建议应用范围(部门id)
     */
    private String suitOrganizationIds;
}
