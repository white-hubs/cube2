package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * 假期余额入参
 */
@Data
public class AttHolidayBalanceParam {
    private Long id;
    /**
     * 人员id
     */
    @NotNull(message = "人员不能为空")
    private Long staffId;
    private String staffName;
    /**
     * 假期规则id
     */
    @NotNull(message = "假期类型不能为空")
    private Long holidayRuleId;
    private String holidayRuleName;
    /**
     * 修改额度
     */
    @NotNull(message = "余额不能为空")
    @Min(value = 0, message = "余额必须大于0")
    private Float availableBalance;
    /**
     * 单位
     */
    @NotNull(message = "余额单位不能为空")
    private String unit;
    /**
     * 有效期
     */
    private LocalDateTime deadline;
    /**
     * 余额修改备注(最后一次修改的备注)
     */
    private String remark;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 审批类型
     */
    private String businessType;
    /**
     * 是否增量修改余额（true为增量修改即balance+=availableBalance,否则直接修改为传入的值
     */
    private Boolean incrementalUpdate;

}
