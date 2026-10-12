package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 假期余额变动记录表
 */
@Data
@Builder
@TableName("att_holiday_balance_record")
@EqualsAndHashCode(callSuper = true)
public class AttHolidayBalanceRecordPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long holidayRuleId;
    private Long staffId;
    private String staffName;
    /**
     * 发放还是扣除(true为添加false为减少
     */
    private Boolean addOrDeduct;
    /**
     * 变动数量(时间戳，加余额为正 扣余额为负
     */
    private Long changeNumber;
    /**
     * 变动前余额
     */
    private Long beforeBalance;
    /**
     * 有效余额id(AttHolidayBalanceDeadlinePO的id)
     */
    private Long deadlineId;
    /**
     * 作废标志(true表示已经作废)
     */
    private Boolean effective;
    /**
     * 作废理由
     */
    private String effectiveRemark;
    /**
     * 作废时间
     */
    private LocalDateTime effectiveTime;
    /**
     * 截至日期(余额有效期
     */
    private LocalDateTime deadline;
    /**
     * 变动备注
     */
    private String remark;
    /**
     * 修改人名称
     */
    private String updateName;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 审批类型
     */
    private String businessType;
}
