package com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 离岗规则
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("att_leave_post_rule")
@EqualsAndHashCode(callSuper = true)
public class AttLeavePostRulePO extends BasicEnhancedTenantEntity<Long> {
    @DiffLogField(name = "规则id")
    private Long id;
    @DiffLogField(name = "规则名称")
    private String name;
    /**
     * 离岗前发起
     */
    @DiffLogField(name = "离岗审批条件", function = "POST_BEFORE_APPLY")
    private Boolean beforeApply;
    /**
     * 最小离岗申请单位（低于该时间不能申请
     */
    @DiffLogField(name = "最小离岗单位")
    private Integer minAskUnit;
    /**
     * 时间计算方式（AttDurationCalcWayEnum（工作日/自然日
     */
    @DiffLogField(name = "离岗时长核算", function = "POST_CALCULATION_WAY")
    private String calculationWay;

}
