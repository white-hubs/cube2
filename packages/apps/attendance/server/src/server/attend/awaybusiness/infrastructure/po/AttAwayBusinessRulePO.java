package com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 出差规则
 */
@Data
@Builder
@TableName("att_away_business_rule")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttAwayBusinessRulePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    @DiffLogField(name = "规则名称")
    private String name;
    /**
     * 提前前发起
     */
    @DiffLogField(name = "出差审批条件", function = "AWAY_BEFORE_APPLY")
    private Boolean beforeApply;
    /**
     * 最小申请单位（低于该时间不能申请
     */
    @DiffLogField(name = "最小出差单位")
    private Integer minAskUnit;
    /**
     * 时间计算方式（AttDurationCalcWayEnum（工作日/自然日
     */
    @DiffLogField(name = "出差时长核算", function = "AWAY_CALCULATION_WAY")
    private String calculationWay;

}
