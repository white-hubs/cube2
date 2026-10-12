package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 假期余额记录分页列表返回对象
 */
@Data
public class AttHolidayBalanceRecordPageResult {
    private Long id;
    private Long holidayRuleId;
    private Long staffId;
    /**
     * 发放还是扣除(true为添加false为减少，0视为添加
     */
    private Boolean addOrDeduct;
    /**
     * 变动数量(时间戳，加余额为正 扣余额为负
     */
    private String changeNumber;
    /**
     * 截至日期(余额有效期
     */
    private LocalDateTime deadline;
    /**
     * 变动备注
     */
    private String remark;
    /**
     * 修改人
     */
    private String updateName;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 作废标志(true表示已经作废)
     */
    private Boolean effective;
    /**
     * 作废时间
     */
    private LocalDateTime effectiveTime;
    /**
     * 作废理由
     */
    private String effectiveRemark;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 审批类型
     */
    private String businessType;

}
